package io.l0neman.themeframework.lib.adapter;

import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.DrawableAttributeSetter;

public class ImageViewAttributeAdapter<T extends ImageView> extends ViewAttributeAdapter<T> {
  public static class Impl extends ImageViewAttributeAdapter<ImageView> {}

  private static final Map<Integer, BaseAttributeSetter<ImageView>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_src, new DrawableAttributeSetter<ImageView>() {
      @Override public boolean onSetView(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
        return true;
      }
    });
  }

  @NonNull @Override public Collection<Integer> themeAttributes() {
    List<Integer> attrs = new LinkedList<>(super.themeAttributes());
    attrs.addAll(sSetters.keySet());
    return attrs;
  }

  @Override public boolean setView(T view, int themeAttribute, int viewAttribute) {
    if (super.setView(view, themeAttribute, viewAttribute))
      return true;

    BaseAttributeSetter<ImageView> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
