package io.l0neman.themeframework.lib.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ProgressBar;

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

public class ProgressBarAttributeAdapter<T extends ProgressBar> extends ViewAttributeAdapter<T> {
  public static class Impl extends ProgressBarAttributeAdapter<ProgressBar> {}

  private static final Map<Integer, BaseAttributeSetter<ProgressBar>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_progressTint, new ColorAttributeSetter<ProgressBar>() {
      @Override public boolean onSetView(ProgressBar view, int color) {
        view.setProgressTintList(ColorStateList.valueOf(color));
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_indeterminateTint, new ColorAttributeSetter<ProgressBar>() {
      @Override public boolean onSetView(ProgressBar view, int color) {
        view.setIndeterminateTintList(ColorStateList.valueOf(color));
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_progressBackgroundTint, new ColorAttributeSetter<ProgressBar>() {
      @Override public boolean onSetView(ProgressBar view, int color) {
        view.setProgressBackgroundTintList(ColorStateList.valueOf(color));
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_indeterminateDrawable, new DrawableAttributeSetter<ProgressBar>() {
      @Override public boolean onSetView(ProgressBar view, Drawable drawable) {
        view.setIndeterminateDrawable(drawable);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_progressDrawable, new DrawableAttributeSetter<ProgressBar>() {
      @Override public boolean onSetView(ProgressBar view, Drawable drawable) {
        view.setProgressDrawable(drawable);
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

    BaseAttributeSetter<ProgressBar> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
