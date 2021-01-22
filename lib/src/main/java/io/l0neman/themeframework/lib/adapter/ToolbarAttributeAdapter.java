package io.l0neman.themeframework.lib.adapter;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;

public class ToolbarAttributeAdapter<T extends Toolbar> extends ViewAttributeAdapter<T> {
  public static class Impl extends ToolbarAttributeAdapter<Toolbar> {}

  private static final Map<Integer, BaseAttributeSetter<Toolbar>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_titleTextColor, new ColorAttributeSetter<Toolbar>() {
      @Override public boolean onSetView(Toolbar view, int color) {
        view.setTitleTextColor(color);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_subtitleTextColor, new ColorAttributeSetter<Toolbar>() {
      @Override public boolean onSetView(Toolbar view, int color) {
        view.setSubtitleTextColor(color);
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

    BaseAttributeSetter<Toolbar> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
