package io.l0neman.themeframework.lib.adapter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.DrawableAttributeSetter;

public class ListViewAttributeAdapter<T extends ListView> extends ViewAttributeAdapter<T> {
  public static final class Impl extends ListViewAttributeAdapter<ListView> {}

  private static final Map<Integer, BaseAttributeSetter<ListView>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_divider, new DrawableAttributeSetter<ListView>() {
      @Override public boolean onSetView(ListView view, Drawable drawable) {
        final int dividerHeight = view.getDividerHeight();
        view.setDivider(drawable);
        if (drawable instanceof ColorDrawable)
          view.setDividerHeight(dividerHeight);

        return true;
      }
    });
  }

  @NonNull @Override public Collection<Integer> themeAttributes() {
    List<Integer> attrs = new ArrayList<>(super.themeAttributes());
    attrs.addAll(sSetters.keySet());
    return attrs;
  }

  @Override public boolean setView(T view, int themeAttribute, int viewAttribute) {
    if (super.setView(view, themeAttribute, viewAttribute))
      return true;

    BaseAttributeSetter<ListView> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
