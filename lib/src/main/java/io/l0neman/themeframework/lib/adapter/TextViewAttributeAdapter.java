package io.l0neman.themeframework.lib.adapter;

import android.util.ArrayMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;

public class TextViewAttributeAdapter<T extends TextView> extends ViewAttributeAdapter<T> {
  public static class Impl extends TextViewAttributeAdapter<TextView> {}

  private final TintableBackgroundViewAttributeAdapter<AppCompatTextView> mTintReuseAdapter =
      new TintableBackgroundViewAttributeAdapter<>();

  private static final Map<Integer, BaseAttributeSetter<TextView>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_textColor, new ColorAttributeSetter<TextView>() {
      @Override public boolean onSetView(TextView view, int color) {
        view.setTextColor(color);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_textColorHint, new ColorAttributeSetter<TextView>() {
      @Override public boolean onSetView(TextView view, int color) {
        view.setHintTextColor(color);
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
    if (mTintReuseAdapter.setView(view, themeAttribute, viewAttribute))
      return true;

    BaseAttributeSetter<TextView> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
