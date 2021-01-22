package io.l0neman.themeframework.lib.adapter;

import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.DrawableAttributeSetter;

public class SpinnerAttributeAdapter<T extends Spinner> extends ViewAttributeAdapter<T> {
  public static final class Impl extends SpinnerAttributeAdapter<Spinner> {}

  private final TintableBackgroundViewAttributeAdapter<AppCompatSpinner> mTintReuseAdapter =
      new TintableBackgroundViewAttributeAdapter<>();

  private static final Map<Integer, BaseAttributeSetter<Spinner>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_popupBackground, new DrawableAttributeSetter<Spinner>() {
      @Override public boolean onSetView(Spinner view, Drawable drawable) {
        view.setPopupBackgroundDrawable(drawable);
        return true;
      }
    });
  }

  @NonNull @Override public Collection<Integer> themeAttributes() {
    List<Integer> attrs = new LinkedList<>(super.themeAttributes());
    attrs.addAll(sSetters.keySet());
    attrs.addAll(mTintReuseAdapter.themeAttributes());
    return attrs;
  }

  @Override public boolean setView(T view, int themeAttribute, int viewAttribute) {
    if (super.setView(view, themeAttribute, viewAttribute))
      return true;

    mTintReuseAdapter.setThemeResources(getThemeResources());
    if (mTintReuseAdapter.setView(view, themeAttribute, viewAttribute)) {
      final int color = getThemeResources().getColor(viewAttribute);
      View selectedView = view.getSelectedView();
      if (selectedView instanceof TextView)
        ((TextView) selectedView).setTextColor(color);

      return true;
    }

    BaseAttributeSetter<Spinner> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
