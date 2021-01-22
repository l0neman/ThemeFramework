package io.l0neman.themeframework.lib;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

/**
 * 帮助获取对应主题下的对应资源
 */
public class ThemeResources {
  private final Resources.Theme mThemeHelper;
  private int mTheme = -1;

  public ThemeResources(Context context) {
    this.mThemeHelper = context.getResources().newTheme();
  }

  public void setTheme(@StyleRes int theme) {
    if (mTheme == theme)
      return;

    mTheme = theme;
    mThemeHelper.applyStyle(mTheme, true);
  }

  public Drawable getDrawable(@AttrRes int attrId) {
    Drawable drawable;
    TypedArray array = mThemeHelper.obtainStyledAttributes(new int[]{attrId});
    drawable = array.getDrawable(0);
    array.recycle();

    return drawable;
  }

  public int getColor(@AttrRes int attrId) {
    int color;
    TypedArray array = mThemeHelper.obtainStyledAttributes(new int[]{attrId});
    color = array.getColor(0, 0);
    array.recycle();

    return color;
  }

  public static int getAttributeByName(Context context, String attrName) {
    return context.getResources().getIdentifier(attrName, "attr", context.getPackageName());
  }
}
