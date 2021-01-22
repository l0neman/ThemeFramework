package io.l0neman.themeframework.lib.adapter;

import android.content.res.ColorStateList;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.TintableBackgroundView;

import java.util.Collection;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.ThemeAttributeAdapter;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;

/**
 * @param <T>
 */
public class TintableBackgroundViewAttributeAdapter<T extends TintableBackgroundView> extends ThemeAttributeAdapter<View> {
  private static final Map<Integer, BaseAttributeSetter<View>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_backgroundTint, new ColorAttributeSetter<View>() {
      @Override public boolean onSetView(View view, int color) {
        if (view instanceof TintableBackgroundView) {
          ((TintableBackgroundView) view).setSupportBackgroundTintList(ColorStateList.valueOf(color));
          return true;
        }

        return false;
      }
    });
  }

  @NonNull @Override public Collection<Integer> themeAttributes() {
    return sSetters.keySet();
  }

  @Override public boolean setView(View view, int themeAttribute, int viewAttribute) {
    BaseAttributeSetter<View> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    return false;
  }
}
