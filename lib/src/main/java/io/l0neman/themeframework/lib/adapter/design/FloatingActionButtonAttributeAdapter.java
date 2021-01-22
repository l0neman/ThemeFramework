package io.l0neman.themeframework.lib.adapter.design;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.l0neman.themeframework.lib.adapter.TintableBackgroundViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ImageViewAttributeAdapter;

public class FloatingActionButtonAttributeAdapter<T extends FloatingActionButton> extends
    ImageViewAttributeAdapter<T> {
  public static class Impl extends FloatingActionButtonAttributeAdapter<FloatingActionButton> {}

  private final TintableBackgroundViewAttributeAdapter<FloatingActionButton> mTintReuseAdapter =
      new TintableBackgroundViewAttributeAdapter<>();

  @NonNull @Override public Collection<Integer> themeAttributes() {
    List<Integer> attrs = new LinkedList<>(super.themeAttributes());
    attrs.addAll(mTintReuseAdapter.themeAttributes());
    return attrs;
  }

  @Override public boolean setView(T view, int themeAttribute, int viewAttribute) {
    if (super.setView(view, themeAttribute, viewAttribute))
      return true;

    mTintReuseAdapter.setThemeResources(getThemeResources());
    return mTintReuseAdapter.setView(view, themeAttribute, viewAttribute);
  }
}
