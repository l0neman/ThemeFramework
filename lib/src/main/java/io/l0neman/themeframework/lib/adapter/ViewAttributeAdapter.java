package io.l0neman.themeframework.lib.adapter;

import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.ThemeAttributeAdapter;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.DrawableAttributeSetter;

/**
 * 所有 View 都支持的主题属性适配器，实现自定义 View 的属性，可以继承这个类型，对主题属性进行扩展
 * 例如 {@link TextViewAttributeAdapter}、{@link ImageViewAttributeAdapter}
 */
public class ViewAttributeAdapter<T extends View> extends ThemeAttributeAdapter<T> {
  public static class Impl extends ViewAttributeAdapter<View> {}

  private static final Map<Integer, BaseAttributeSetter<View>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_background, new DrawableAttributeSetter<View>() {
      @Override public boolean onSetView(View view, Drawable drawable) {
        view.setBackground(drawable);
        return true;
      }
    });
  }

  @NonNull @Override public Collection<Integer> themeAttributes() {
    return Collections.singletonList(R.styleable.Attributes_Layout_tf_background);
  }

  @Override public boolean setView(T view, int themeAttribute, int viewAttribute) {
    // 表驱动方式处理属性的设置，避免 if else if else...
    BaseAttributeSetter<View> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
