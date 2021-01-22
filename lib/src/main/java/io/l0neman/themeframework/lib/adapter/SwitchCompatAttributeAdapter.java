package io.l0neman.themeframework.lib.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;
import io.l0neman.themeframework.lib.setter.DrawableAttributeSetter;

public class SwitchCompatAttributeAdapter<T extends SwitchCompat> extends CompoundButtonAttributeAdapter<T> {
  public static class Impl extends SwitchCompatAttributeAdapter<SwitchCompat> {}

  private static final Map<Integer, BaseAttributeSetter<SwitchCompat>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_thumb, new DrawableAttributeSetter<SwitchCompat>() {
      @Override public boolean onSetView(
          SwitchCompat view, Drawable drawable) {
        view.setThumbDrawable(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_thumbTint, new ColorAttributeSetter<SwitchCompat>() {
      @Override public boolean onSetView(SwitchCompat view, int color) {
        view.setThumbTintList(ColorStateList.valueOf(color));
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_track, new DrawableAttributeSetter<SwitchCompat>() {
      @Override public boolean onSetView(SwitchCompat view, Drawable drawable) {
        view.setTrackDrawable(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_trackTint, new ColorAttributeSetter<SwitchCompat>() {
      @Override public boolean onSetView(SwitchCompat view, int color) {
        view.setTrackTintList(ColorStateList.valueOf(color));
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

    BaseAttributeSetter<SwitchCompat> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
