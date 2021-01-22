package io.l0neman.themeframework.lib;

import android.util.Log;

/**
 * ThemeFramework 调试工具
 */
class TfDebug {
  private static final String TAG = "TF#";
  private static final boolean DEBUG = BuildConfig.DEBUG;

  public static void logD(String tag, String log) {
    if (DEBUG)
      Log.d(TAG + tag, log);
  }

  public static void logW(String tag, String log) {
    if (DEBUG)
      Log.w(TAG + tag, log);
  }
}
