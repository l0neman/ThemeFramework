package io.l0neman.themeframework.lib.adapter.design;

import android.content.res.ColorStateList;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.themeframework.lib.AttributeSetter;
import io.l0neman.themeframework.lib.R;
import io.l0neman.themeframework.lib.adapter.ViewAttributeAdapter;
import io.l0neman.themeframework.lib.setter.BaseAttributeSetter;
import io.l0neman.themeframework.lib.setter.ColorAttributeSetter;

public class TabLayoutAttributeAdapter<T extends TabLayout> extends ViewAttributeAdapter<T> {
  public static class Impl extends TabLayoutAttributeAdapter<TabLayout> {}

  private static final Map<Integer, BaseAttributeSetter<TabLayout>> sSetters = new ArrayMap<>();

  static {
    sSetters.put(R.styleable.Attributes_Layout_tf_tabSelectedTextColor, null); // 单独处理
    sSetters.put(R.styleable.Attributes_Layout_tf_tabTextColor, null);         // 单独处理
    sSetters.put(R.styleable.Attributes_Layout_tf_tabIndicatorColor, new ColorAttributeSetter<TabLayout>() {
      @Override public boolean onSetView(TabLayout view, int color) {
        view.setSelectedTabIndicatorColor(color);
        return true;
      }
    });
    sSetters.put(R.styleable.Attributes_Layout_tf_tabRippleColor, new ColorAttributeSetter<TabLayout>() {
      @Override public boolean onSetView(TabLayout view, int color) {
        view.setTabRippleColor(ColorStateList.valueOf(color));
        return true;
      }
    });
  }

  private int mNormalColor;
  private int mSelectedColor;

  @NonNull @Override public Collection<Integer> themeAttributes() {
    List<Integer> attrs = new LinkedList<>(super.themeAttributes());
    attrs.addAll(sSetters.keySet());
    return attrs;
  }

  @Override public boolean setView(T view, int themeAttribute, int viewAttribute) {
    if (super.setView(view, themeAttribute, viewAttribute))
      return true;

    BaseAttributeSetter<TabLayout> viewAttributeSetter = sSetters.get(themeAttribute);
    if (viewAttributeSetter != null) {
      viewAttributeSetter.setThemeResources(getThemeResources());
      return viewAttributeSetter.setView(view, viewAttribute);
    }

    if (themeAttribute == R.styleable.Attributes_Layout_tf_tabSelectedTextColor) {
      mSelectedColor = getThemeResources().getColor(viewAttribute);
      view.setTabTextColors(mNormalColor, mSelectedColor);
      return true;
    } else if (themeAttribute == R.styleable.Attributes_Layout_tf_tabTextColor) {
      mNormalColor = getThemeResources().getColor(viewAttribute);
      view.setTabTextColors(mNormalColor, mSelectedColor);
      return true;
    }

    return false;
  }
}
