package io.l0neman.themeframework.lib.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;
import io.l0neman.themeframework.lib.setter.DrawableAttributeSetter;

public class SeekBarAttributeAdapter<T extends SeekBar> extends ProgressBarAttributeAdapter<T> {
  public static class Impl extends SeekBarAttributeAdapter<SeekBar> {}

  private static final Map<Integer, BaseAttributeSetter<SeekBar>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_thumb, new DrawableAttributeSetter<SeekBar>() {
      @Override public boolean onSetView(SeekBar view, Drawable drawable) {
        view.setThumb(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_thumbTint, new ColorAttributeSetter<SeekBar>() {
      @Override public boolean onSetView(SeekBar view, int color) {
        view.setThumbTintList(ColorStateList.valueOf(color));
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

    BaseAttributeSetter<SeekBar> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
