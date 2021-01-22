package io.l0neman.themeframework.lib.setter;

import android.view.View;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.ThemeResources;

/**
 * 基类，提供设置 View 时必须的资源
 *
 * @param <T> View 类型泛型
 */
public abstract class BaseAttributeSetter<T extends View> implements AttributeSetter<T> {
  private ThemeResources mThemeResources;

  /**
   * 提供一个主题资源对象，在设置 View 时可使用
   *
   * @param mThemeResources 主题资源对象
   */
  public void setThemeResources(ThemeResources mThemeResources) {
    this.mThemeResources = mThemeResources;
  }

  /** 获取主题资源对象 */
  public ThemeResources getThemeResources() {
    return mThemeResources;
  }
}
