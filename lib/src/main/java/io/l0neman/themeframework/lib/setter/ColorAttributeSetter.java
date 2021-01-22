package io.l0neman.themeframework.lib.setter;

import android.view.View;

import androidx.annotation.ColorInt;

/**
 * 根据属性为 View 设置色彩的复用类
 *
 * @param <T> View 泛型
 */
public abstract class ColorAttributeSetter<T extends View> extends BaseAttributeSetter<T> {

  @Override public boolean setView(T view, int attribute) {
    final int color = getThemeResources().getColor(attribute);
    return onSetView(view, color);
  }

  public abstract boolean onSetView(T view, @ColorInt int color);
}
