package io.l0neman.themeframework.lib.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CompoundButton;

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

public class CompoundButtonAttributeAdapter<T extends CompoundButton> extends TextViewAttributeAdapter<T> {
  public static class Impl extends CompoundButtonAttributeAdapter<CompoundButton> {}

  private static final Map<Integer, BaseAttributeSetter<CompoundButton>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_button, new DrawableAttributeSetter<CompoundButton>() {
      @Override public boolean onSetView(CompoundButton view, Drawable drawable) {
        view.setButtonDrawable(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_buttonTint, new ColorAttributeSetter<CompoundButton>() {
      @Override public boolean onSetView(CompoundButton view, int color) {
        view.setButtonTintList(ColorStateList.valueOf(color));
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

    BaseAttributeSetter<CompoundButton> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
