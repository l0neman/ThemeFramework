package io.l0neman.themeframework.lib.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Switch;

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

@SuppressLint("UseSwitchCompatOrMaterialCode") // 已标记过时
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated // 使用 SwitchCompat，thumbTint 和 trackTint 可以兼容低版本系统
public class SwitchAttributeAdapter<T extends Switch> extends CompoundButtonAttributeAdapter<T> {
  public static class Impl extends SwitchAttributeAdapter<Switch> {}

  private static final Map<Integer, BaseAttributeSetter<Switch>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_thumb, new DrawableAttributeSetter<Switch>() {
      @Override public boolean onSetView(
          Switch view, Drawable drawable) {
        view.setThumbDrawable(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_thumbTint, new ColorAttributeSetter<Switch>() {
      @Override public boolean onSetView(Switch view, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
          view.setThumbTintList(ColorStateList.valueOf(color));

        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_track, new DrawableAttributeSetter<Switch>() {
      @Override public boolean onSetView(Switch view, Drawable drawable) {
        view.setTrackDrawable(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_trackTint, new ColorAttributeSetter<Switch>() {
      @Override public boolean onSetView(Switch view, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
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

    BaseAttributeSetter<Switch> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
