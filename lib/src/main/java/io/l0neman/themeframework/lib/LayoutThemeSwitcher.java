package io.l0neman.themeframework.lib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 为一个布局提供主题切换功能
 */
class LayoutThemeSwitcher {
  private static final String TAG = LayoutThemeSwitcher.class.getSimpleName();

  private final LayoutThemeAttributesCollector mLayoutThemeAttributesCollector;
  private final ThemeResources mThemeResources;

  public LayoutThemeSwitcher(Context context) {
    this.mLayoutThemeAttributesCollector = new LayoutThemeAttributesCollector(context);
    this.mThemeResources = new ThemeResources(context);
  }

  /**
   * 绑定一个布局，绑定后即可进行主题切换
   *
   * @param root   布局根 View
   * @param layout 布局文件资源 Id
   */
  public void bind(View root, @LayoutRes int layout) {
    mLayoutThemeAttributesCollector.collectInfo(root, layout);
  }

  /**
   * 设置或添加 View 对应的主题属性适配器
   *
   * @param view             View
   * @param attributeAdapter 主题属性适配器
   * @param <T>              适配器泛型
   */
  public <T extends View> void setView(T view, ThemeAttributeAdapter<T> attributeAdapter) {
    mLayoutThemeAttributesCollector.setInfo(view, attributeAdapter);
  }

  public <T extends View> ThemeAttributeAdapter<T> getAttributeAdapter(T view) {
    return mLayoutThemeAttributesCollector.getInfo(view);
  }

  /**
   * 切换主题
   *
   * @param theme 主题资源 Id
   */
  public void switchTheme(@StyleRes int theme) {
    mThemeResources.setTheme(theme);
    if (mLayoutThemeAttributesCollector.getViewThemeAttributeMap().isEmpty())
      return;

    setAllViewThemes();
  }

  private void setAllViewThemes() {
    Set<Map.Entry<View, ThemeAttributeAdapter<? extends View>>> entries = mLayoutThemeAttributesCollector
        .getViewThemeAttributeMap().entrySet();

    for (Map.Entry<View, ThemeAttributeAdapter<? extends View>> entry : entries) {
      View key = entry.getKey();
      ThemeAttributeAdapter<? extends View> value = entry.getValue();
      value.setView(key, mThemeResources);
    }
  }

  /** 回收主题切换所需的资源 */
  public void recycle() {
    mLayoutThemeAttributesCollector.cleanInfo();
  }
}
