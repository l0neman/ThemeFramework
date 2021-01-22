package io.l0neman.themeframework.lib;

import androidx.annotation.StyleRes;

/** 主题切换监听器 */
public interface ThemeChangeListener {
  /**
   * 切换到主题
   *
   * @param theme 当前主题 styleable 值
   */
  void onThemeChanged(@StyleRes int theme);
}
