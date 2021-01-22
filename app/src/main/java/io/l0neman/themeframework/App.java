package io.l0neman.themeframework;

import android.app.Application;

import io.l0neman.themeframework.lib.ThemeFramework;

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    ThemeFramework.getInstance().setup(this, R.style.AppTheme);
  }

}
