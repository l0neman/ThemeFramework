package io.l0neman.themeframework.lib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.l0neman.themeframework.lib.adapter.ViewAttributeAdapter;

/**
 * 主题框架统一接口
 *
 * @author l0neman
 * @version 0.1
 */
public class ThemeFramework {
  private static final String TAG = ThemeFramework.class.getSimpleName();

  private static final String SP_FILE_THEME = "theme";
  private static final String SP_KEY_THEME = "theme_id";

  private static final ThemeFramework INSTANCE = new ThemeFramework();

  private ThemeResources mThemeResources;

  public static ThemeFramework getInstance() {
    return INSTANCE;
  }

  @StyleRes private int mTheme = -1;
  private final List<ThemeChangeListener> mListeners = new LinkedList<>();

  private Application mApp;

  private final Set<AppCompatActivity> mBoundActivities = new ArraySet<>();
  private final Set<Fragment> mBoundFragments = new ArraySet<>();
  private final Map<LifecycleOwner, LifecycleOwnerThemeSwitcher> mLifecycleOwnerThemeSwitchers = new ArrayMap<>();

  /**
   * 初始化 ThemeFramework，设置默认主题
   * ，建议在 Application#onCreate 中调用
   *
   * @param app          提供 Application Context
   * @param defaultTheme 首次应用默认主题
   */
  public void setup(Application app, @StyleRes int defaultTheme) {
    this.mApp = app;
    mThemeResources = new ThemeResources(app);

    final int themeFromSp = getThemeFromSp();
    final int setTheme = themeFromSp == -1 ? defaultTheme : themeFromSp;

    switchTheme(setTheme);
    handleActivityTheme();
  }

  private void handleActivityTheme() {
    mApp.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override public void onActivityCreated(
          @NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activity.setTheme(getTheme());
      }

      @Override public void onActivityStarted(@NonNull Activity activity) {}

      @Override public void onActivityResumed(@NonNull Activity activity) {}

      @Override public void onActivityPaused(@NonNull Activity activity) {}

      @Override public void onActivityStopped(@NonNull Activity activity) {}

      @Override public void onActivitySaveInstanceState(
          @NonNull Activity activity, @NonNull Bundle outState) {}

      @Override public void onActivityDestroyed(@NonNull Activity activity) {}
    });
  }

  /**
   * 获取 ThemeAttributeAdapter 的回调
   *
   * @param <T> 对应 View 泛型
   */
  public interface ThemeAttributeAdapterCallback<T extends View> {
    void onThemeAttributeAdapter(ThemeAttributeAdapter<T> attributeAdapter);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Activity APIs
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 绑定 Activity，绑定后可进行主题切换，有实时切换主题需求的 Activity 才需要绑定
   *
   * @param activity Activity
   * @param root     Activity 根布局 View
   * @param layout   Activity 根布局资源 id
   */
  public void bind(AppCompatActivity activity, View root, @LayoutRes int layout) {
    getLifecycleOwnerLayoutThemeSwitcher(activity).bind(activity, root, layout);
    activity.getLifecycle().addObserver(new LifecycleEventObserver() {
      @Override
      public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        AppCompatActivity activity = (AppCompatActivity) source;
        switch (event) {
        case ON_CREATE:
          mBoundActivities.add(activity);
          break;
        case ON_DESTROY:
          mBoundActivities.remove(activity);
          removeActivityLayoutThemeSwitcher(activity);
          activity.getLifecycle().removeObserver(this);
          break;
        }
      }
    });
  }

  /**
   * 绑定 Activity 中的其他布局以外的布局，例如 ListView 和 RecyclerView 的 item，
   * 或 Dialog 等单独使用布局且不在 Activity 布局树中的 UI 组件
   *
   * @param activity Activity
   * @param root     UI 组件的根布局 view
   * @param layout   UI 组件的根布局资源 id
   */
  public void bindView(AppCompatActivity activity, View root, @LayoutRes int layout) {
    getLifecycleOwnerLayoutThemeSwitcher(activity).bind(root, layout);
  }

  /**
   * 设置已绑定布局根 View（调用了 bind 和 bindView 的布局）中的子 View 对应的主题属性适配器，
   * 设置后会替换原有通过 bind 添加的默认主题属性适配器
   *
   * @param activity         Activity
   * @param root             已绑定布局根 View，即调用 bind 和 bindView 方法中的 root 参数
   * @param view             已绑定布局根 View 子 View
   * @param attributeAdapter View 对应的主题属性适配器
   * @param <T>              属性适配器泛型
   */
  public <T extends View> void setView(AppCompatActivity activity, View root, T view,
                                       ThemeAttributeAdapter<T> attributeAdapter) {
    getLifecycleOwnerLayoutThemeSwitcher(activity).setView(root, view, attributeAdapter);
  }

  /**
   * 获取已绑定布局根 View（调用了 bind 和 bindView 的布局）中的子 View 对应的主题属性适配器
   *
   * @param activity Activity
   * @param root     已绑定布局根 View，即调用 bind 和 bindView 方法中的 root 参数
   * @param view     已绑定布局根 View 子 View
   * @param callback 主题属性适配器回调，会在 bind 之后返回
   * @param <T>      属性适配器泛型
   */
  public <T extends View> void getAttributeAdapter(AppCompatActivity activity, View root, T view,
                                                   ThemeAttributeAdapterCallback<T> callback) {
    getLifecycleOwnerLayoutThemeSwitcher(activity).getAttributeAdapter(root, view,
        callback::onThemeAttributeAdapter);
  }

  /**
   * 动态添加单个 View 到已绑定过布局的 Activity 中，需要提供对应的主题属性适配器，用于处理 View 主题切换
   *
   * @param activity         Activity
   * @param view             View
   * @param attributeAdapter View 对应的主题属性适配器
   * @param <T>              属性适配器泛型
   */
  public <T extends View> void addView(AppCompatActivity activity, T view,
                                       ThemeAttributeAdapter<T> attributeAdapter) {
    getLifecycleOwnerLayoutThemeSwitcher(activity).addView(view, attributeAdapter);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Fragment APIs
  ///////////////////////////////////////////////////////////////////////////

  /**
   * 用于 Fragment
   *
   * @see #bind(AppCompatActivity, View, int)
   */
  public void bind(Fragment fragment, View root, @LayoutRes int layout) {
    getLifecycleOwnerLayoutThemeSwitcher(fragment).bind(fragment, root, layout);
    fragment.getLifecycle().addObserver(new LifecycleEventObserver() {
      @Override
      public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        Fragment fragment = (Fragment) source;
        switch (event) {
        case ON_CREATE:
          mBoundFragments.add(fragment);
          break;
        case ON_DESTROY:
          mBoundFragments.remove(fragment);
          removeActivityLayoutThemeSwitcher(fragment);
          fragment.getLifecycle().removeObserver(this);
          break;
        }
      }
    });
  }

  /**
   * 用于 Fragment
   *
   * @see #bindView(AppCompatActivity, View, int)
   */
  public void bindView(Fragment fragment, View root, @LayoutRes int layout) {
    getLifecycleOwnerLayoutThemeSwitcher(fragment).bind(root, layout);
  }

  /**
   * 用于 Fragment
   *
   * @see #setView(AppCompatActivity, View, View, ThemeAttributeAdapter)
   */
  public <T extends View> void setView(Fragment fragment, View root, T view,
                                       ViewAttributeAdapter<T> attributeAdapter) {
    getLifecycleOwnerLayoutThemeSwitcher(fragment).setView(root, view, attributeAdapter);
  }

  /**
   * 用于 Fragment
   *
   * @see #getAttributeAdapter(AppCompatActivity, View, View, ThemeAttributeAdapterCallback)
   */
  public <T extends View> void getAttributeAdapter(Fragment fragment, View root, T view,
                                                   ThemeAttributeAdapterCallback<T> callback) {
    getLifecycleOwnerLayoutThemeSwitcher(fragment).getAttributeAdapter(root, view,
        callback::onThemeAttributeAdapter);
  }

  /**
   * 用于 Fragment
   *
   * @see #addView(AppCompatActivity, View, ThemeAttributeAdapter)
   */
  public <T extends View> void addView(Fragment fragment, T view,
                                       ViewAttributeAdapter<T> attributeAdapter) {
    getLifecycleOwnerLayoutThemeSwitcher(fragment).addView(view, attributeAdapter);
  }

  private LifecycleOwnerThemeSwitcher getLifecycleOwnerLayoutThemeSwitcher(LifecycleOwner lifecycleOwner) {
    LifecycleOwnerThemeSwitcher lifecycleOwnerThemeSwitcher = mLifecycleOwnerThemeSwitchers.get(lifecycleOwner);
    if (lifecycleOwnerThemeSwitcher == null) {
      lifecycleOwnerThemeSwitcher = new LifecycleOwnerThemeSwitcher(mApp);
      mLifecycleOwnerThemeSwitchers.put(lifecycleOwner, lifecycleOwnerThemeSwitcher);
    }

    return lifecycleOwnerThemeSwitcher;
  }

  private void removeActivityLayoutThemeSwitcher(LifecycleOwner lifecycleOwner) {
    mLifecycleOwnerThemeSwitchers.remove(lifecycleOwner);
  }

  /**
   * 切换主题，调用过 {@link #bind(AppCompatActivity, View, int)} 的 Activity 将会进行实时切换，
   * 所有注册的 {@link ThemeChangeListener} 将被通知
   *
   * @param theme 需要切换的主题，如果和最后一次主题相同，将不执行任何动作
   */
  public void switchTheme(@StyleRes int theme) {
    if (this.mTheme == theme)
      return;

    this.mTheme = theme;
    putThemeTpSp(mTheme);

    mApp.setTheme(theme);

    notifyThemeChanged(theme);
  }

  private void notifyThemeChanged(int theme) {
    for (AppCompatActivity activity : mBoundActivities)
      switchBoundActivityTheme(activity);

    for (Fragment fragment : mBoundFragments)
      switchLifecycleOwnerTheme(fragment);

    for (ThemeChangeListener listener : mListeners)
      listener.onThemeChanged(theme);
  }

  private void switchBoundActivityTheme(AppCompatActivity activity) {
    activity.setTheme(mTheme);
    switchLifecycleOwnerTheme(activity);
  }

  private void switchLifecycleOwnerTheme(LifecycleOwner lifecycleOwner) {
    getLifecycleOwnerLayoutThemeSwitcher(lifecycleOwner).switchTheme(mTheme);
  }

  /**
   * 注册主题切换监听器
   *
   * @param listener 监听器对象
   */
  public void registerThemeChangedListener(ThemeChangeListener listener) {
    if (listener == null)
      return;

    mListeners.add(listener);
  }

  /**
   * 解测注册主题切换监听器
   *
   * @param listener 监听器对象
   */
  public void unregisterThemeChangedListener(ThemeChangeListener listener) {
    mListeners.remove(listener);
  }

  /**
   * 获取当前主题
   *
   * @return 当前主题 styleable 值
   */
  public int getTheme() {
    return mTheme;
  }

  /**
   * 提供主题资源获取工具
   */
  public ThemeResources getThemeResources() {
    mThemeResources.setTheme(mTheme);
    return mThemeResources;
  }

  ///////////////////////////////////////////////////////////////////////////
  // SharePreferences Utils
  ///////////////////////////////////////////////////////////////////////////

  private void putThemeTpSp(int theme) {
    SharedPreferences sp = mApp.getSharedPreferences(SP_FILE_THEME, Context.MODE_PRIVATE);
    sp.edit().putInt(SP_KEY_THEME, theme).apply();
  }

  private int getThemeFromSp() {
    SharedPreferences sp = mApp.getSharedPreferences(SP_FILE_THEME, Context.MODE_PRIVATE);
    return sp.getInt(SP_KEY_THEME, -1);
  }
}
