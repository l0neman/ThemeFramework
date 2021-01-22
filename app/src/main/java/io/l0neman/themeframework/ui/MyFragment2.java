package io.l0neman.themeframework.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.l0neman.themeframework.R;
import io.l0neman.themeframework.lib.ThemeFramework;

public class MyFragment2 extends Fragment {
  @Nullable @Override public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = View.inflate(getContext(), R.layout.fragment_next_page2, null);
    ThemeFramework.getInstance().bind(this, root, R.layout.fragment_next_page2);
    return root;
  }
}