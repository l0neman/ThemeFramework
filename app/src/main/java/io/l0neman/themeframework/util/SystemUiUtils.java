package io.l0neman.themeframework.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

/**
 * Created by l0neman on 2020/03/15.
 */
public class SystemUiUtils {

  @RequiresApi(Build.VERSION_CODES.KITKAT)
  public static void setUiUnderTheStatusBar(Activity ui) {
    Window window = ui.getWindow();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

      int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
      systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      window.getDecorView().setSystemUiVisibility(systemUiVisibility);

      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      return;
    }

    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  public static void setStatueBarColor(Activity ui, @ColorInt int color) {
    ui.getWindow().setStatusBarColor(color);
  }

  @RequiresApi(Build.VERSION_CODES.KITKAT)
  public static void setUiUnderTheNavigationBar(Activity ui) {
    Window window = ui.getWindow();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

      int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
      systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      window.getDecorView().setSystemUiVisibility(systemUiVisibility);

      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      return;
    }

    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  public static void setNavigationBarColor(Activity ui, @ColorInt int color) {
    ui.getWindow().setNavigationBarColor(color);
  }

  @RequiresApi(Build.VERSION_CODES.M)
  public static void setLightStatusBar(Activity ui) {
    int systemUiVisibility = ui.getWindow().getDecorView().getSystemUiVisibility();
    systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    ui.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
  }

  @RequiresApi(Build.VERSION_CODES.M)
  public static void setDarkStatusBar(Activity ui) {
    int systemUiVisibility = ui.getWindow().getDecorView().getSystemUiVisibility();
    systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    ui.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
  }

  @RequiresApi(Build.VERSION_CODES.O)
  public static void setLightNavigationBar(Activity ui) {
    int systemUiVisibility = ui.getWindow().getDecorView().getSystemUiVisibility();
    systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
    ui.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
  }

  @RequiresApi(Build.VERSION_CODES.O)
  public static void setDarkNavigationBar(Activity ui) {
    int systemUiVisibility = ui.getWindow().getDecorView().getSystemUiVisibility();
    systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
    ui.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
  }
}
