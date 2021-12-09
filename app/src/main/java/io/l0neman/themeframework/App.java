package io.l0neman.themeframework;

import android.app.Application;

import io.l0neman.themeframework.lib.ThemeFramework;

public class App extends Application {

  private static final int[] THEME_ARRAY = {
    R.style.AppTheme_Default,
    R.style.AppTheme_Black,
    R.style.AppTheme_White,
  };

  @Override public void onCreate() {
    super.onCreate();
    ThemeFramework.getInstance().setup(this, new ThemeFramework.ThemeValueAdapter() {
      @Override
      public int getThemeId(int index) {
        return THEME_ARRAY[index];
      }

      @Override
      public int getThemeCount() {
        return THEME_ARRAY.length;
      }
    });
  }

}
