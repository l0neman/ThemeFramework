package io.l0neman.themeframework.lib.adapter;

import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;

public class CardViewAttributeAdapter<T extends CardView> extends ViewAttributeAdapter<T> {
  public static class Impl extends CardViewAttributeAdapter<CardView> {}

  private static final Map<Integer, BaseAttributeSetter<CardView>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_cardBackgroundColor, new ColorAttributeSetter<CardView>() {
      @Override public boolean onSetView(CardView view, int color) {
        view.setCardBackgroundColor(color);
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

    BaseAttributeSetter<CardView> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
