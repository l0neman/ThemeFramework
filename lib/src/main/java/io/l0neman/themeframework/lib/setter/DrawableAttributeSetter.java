package io.l0neman.themeframework.lib.setter;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 根据属性为 View 设置可绘制资源的复用类
 *
 * @param <T> View 泛型
 */
public abstract class DrawableAttributeSetter<T extends View> extends BaseAttributeSetter<T> {

  @Override public boolean setView(T view, int attribute) {
    Drawable drawable = getThemeResources().getDrawable(attribute);
    return onSetView(view, drawable);
  }

  public abstract boolean onSetView(T view, Drawable drawable);
}
