package io.l0neman.themeframework.lib;

import android.content.Context;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 为一个 Activity 或者 Fragment 提供主题切换功能
 * <p>
 * 一个 Activity 和 Fragment 可能包含多个布局，需要同时管理多个布局的主题群切换
 * <p>
 * Activity 和 Fragment 有一个共同点，都具有完整的生命周期，他们都是 LifecycleOwner
 */
class LifecycleOwnerThemeSwitcher {
  private static final String TAG = LifecycleOwnerThemeSwitcher.class.getSimpleName();

  // 以 View 做 key，而不是 layoutId
  private final Map<View, LayoutThemeSwitcher> mLayoutSwitcherMap = new ArrayMap<>();
  private LayoutThemeSwitcher mSingleViewSwitcher;
  private final Context mContext;
  private final LifecycleOwnerBindTask mLifecycleOwnerBindTask = new LifecycleOwnerBindTask();

  public LifecycleOwnerThemeSwitcher(Context context) {
    this.mContext = context;
  }

  /*
   * 在 Activity 和 Fragment 绑定后执行任务的工具，主线程
   */
  private static final class LifecycleOwnerBindTask {
    private boolean mIsBinding = false;
    private boolean mIsBound = false;

    private final List<Runnable> mTasks = new LinkedList<>();

    private void setBinding() {
      mIsBinding = true;
    }

    private void setBound() {
      mIsBound = true;
      for (Runnable task : mTasks)
        task.run();
    }

    private void submit(Runnable task) {
      if (mIsBound) {
        task.run();
        return;
      }

      if (mIsBinding) {
        mTasks.add(task);
        return;
      }

      throw new IllegalStateException("not bind");
    }
  }

  /**
   * 绑定一个 Activity 或 Fragment 的布局，绑定后可以切换主题
   *
   * @param lifecycleOwner Activity 或 Fragment 对象
   * @param root           Activity 或 Fragment 根布局 View
   * @param layout         Activity 或 Fragment 根布局 id
   */
  public void bind(LifecycleOwner lifecycleOwner, View root, @LayoutRes int layout) {
    mLifecycleOwnerBindTask.setBinding();
    lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
      @Override
      public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
        case ON_CREATE:
          getLayoutThemeSwitcher(root).bind(root, layout);
          mLifecycleOwnerBindTask.setBound();
          TfDebug.logD(TAG, "bind activity content view: " + lifecycleOwner);
          break;
        case ON_DESTROY:
          recycle();
          TfDebug.logD(TAG, "unbind activity content view: " + lifecycleOwner);
          lifecycleOwner.getLifecycle().removeObserver(this);
          break;
        }
      }
    });
  }

  /**
   * 绑定一个 Activity 或 Fragment 中的独立添加的 View
   *
   * @param root   View 本身
   * @param layout View 布局 id
   */
  public void bind(View root, @LayoutRes int layout) {
    mLifecycleOwnerBindTask.submit(() -> {
      if (mLayoutSwitcherMap.containsKey(root))
        throw new IllegalAccessError("Do not allow view to be bound multiple times, view: " + root);

      getLayoutThemeSwitcher(root).bind(root, layout);
    });
  }

  /**
   * 设置已绑定的 View（调用了 bind 和 bindView 的布局）中的子 View 对应的主题属性适配器，
   * 设置后会替换原有通过 bind 添加的默认主题属性适配器
   *
   * @param root             已绑定 view
   * @param view             已绑定 view 的子 view
   * @param attributeAdapter 指定主题属性适配器
   * @param <T>              主题属性适配器泛型
   */
  public <T extends View> void setView(View root, T view, ThemeAttributeAdapter<T> attributeAdapter) {
    mLifecycleOwnerBindTask.submit(() -> {
      if (!mLayoutSwitcherMap.containsKey(root))
        throw new IllegalAccessError("please bind the root view first");

      getLayoutThemeSwitcher(root).setView(view, attributeAdapter);
    });
  }

  /**
   * 获取已绑定布局 View（调用了 bind 和 bindView 的布局）中的子 View 对应的主题属性适配器，
   *
   * @param root     已绑定 view
   * @param view     已绑定 view 的子 view
   * @param callback 在布局被 bind 后获取到主题属性后回调，时机是 Activity 或 Fragment 的 onCreate 执行后
   * @param <T>      主题属性适配器泛型
   */
  public <T extends View> void getAttributeAdapter(View root, T view, ThemeAttributeAdapterCallback<T> callback) {
    mLifecycleOwnerBindTask.submit(() ->
        callback.onThemeAttributeAdapter(getLayoutThemeSwitcher(root).getAttributeAdapter(view)));
  }

  /** 获取主题属性适配器回调 */
  public interface ThemeAttributeAdapterCallback<T extends View> {
    void onThemeAttributeAdapter(ThemeAttributeAdapter<T> attributeAdapter);
  }

  /**
   * 设置 View 对应的主题属性适配器
   *
   * @param view             View
   * @param attributeAdapter 主题属性适配器
   * @param <T>              适配器泛型
   */
  public <T extends View> void addView(T view, ThemeAttributeAdapter<T> attributeAdapter) {
    if (mSingleViewSwitcher == null)
      mSingleViewSwitcher = new LayoutThemeSwitcher(mContext);

    mSingleViewSwitcher.setView(view, attributeAdapter);
  }

  /**
   * 切换此 Activity 或 Fragment 的主题，包含调用了 bind 的所有 View
   *
   * @param theme 主题资源值
   */
  public void switchTheme(@StyleRes int theme) {
    if (mSingleViewSwitcher != null)
      mSingleViewSwitcher.switchTheme(theme);

    for (Map.Entry<View, LayoutThemeSwitcher> layoutThemeSwitcherEntry : mLayoutSwitcherMap.entrySet())
      layoutThemeSwitcherEntry.getValue().switchTheme(theme);
  }

  /* 回收主题属性相关资源 */
  private void recycle() {
    for (Map.Entry<View, LayoutThemeSwitcher> layoutThemeSwitcherEntry : mLayoutSwitcherMap.entrySet())
      layoutThemeSwitcherEntry.getValue().recycle();

    mLayoutSwitcherMap.clear();

    if (mSingleViewSwitcher != null)
      mSingleViewSwitcher.recycle();
  }

  private LayoutThemeSwitcher getLayoutThemeSwitcher(View root) {
    LayoutThemeSwitcher layoutThemeSwitcher = mLayoutSwitcherMap.get(root);
    if (layoutThemeSwitcher == null) {
      layoutThemeSwitcher = new LayoutThemeSwitcher(mContext);
      mLayoutSwitcherMap.put(root, layoutThemeSwitcher);
    }

    return layoutThemeSwitcher;
  }
}
