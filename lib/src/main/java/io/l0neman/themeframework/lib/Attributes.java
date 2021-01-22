package io.l0neman.themeframework.lib;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

// 仅用于属性提示，attrs.xml - Attributes_Layout
public class Attributes extends ViewGroup {
  public Attributes(Context context) {
    super(context);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {}
}
