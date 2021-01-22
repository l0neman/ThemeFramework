package io.l0neman.themeframework;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.l0neman.themeframework.base.BaseActivity;
import io.l0neman.themeframework.dialog.SettingsDialog;
import io.l0neman.themeframework.lib.ThemeAttributeAdapter;
import io.l0neman.themeframework.lib.ThemeAttributeAdapterManager;
import io.l0neman.themeframework.lib.ThemeChangeListener;
import io.l0neman.themeframework.lib.ThemeFramework;
import io.l0neman.themeframework.lib.adapter.TextViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ViewAttributeAdapter;
import io.l0neman.themeframework.ui.NextActivity;

public class MainActivity extends BaseActivity implements ThemeChangeListener {
  private SettingsDialog mSettingsDialog;
  private ActionBarDrawerToggle mDrawerToggle;
  private RecyclerView mMenuList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    View root = findViewById(R.id.root);
    ThemeFramework.getInstance().bind(this, root, R.layout.activity_main);
    ThemeFramework.getInstance().registerThemeChangedListener(this);
    ThemeFramework.getInstance().getAttributeAdapter(this, root, getToolbar(),
        attributeAdapter -> attributeAdapter.setOnSetViewListener((view, tr) -> {
              // 刷新 Toolbar 右上角主题菜单图标
              view.getMenu().getItem(0).setIcon(tr.getDrawable(R.attr.ic_style));

              // 刷新 Toolbar 左上角动态图标
              mDrawerToggle.setDrawerArrowDrawable(new DrawerArrowDrawable(this));
            }
        ));

    mSettingsDialog = new SettingsDialog(this);
    initDrawer();
  }

  @Override public void onThemeChanged(int theme) {
    // 刷新左侧菜单图标
    Objects.requireNonNull(mMenuList.getAdapter()).notifyDataSetChanged();
    setSystemBarTheme();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ThemeFramework.getInstance().unregisterThemeChangedListener(this);
  }

  private static class MyHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView icon;

    public MyHolder(@NonNull View itemView) {
      super(itemView);
      this.title = itemView.findViewById(R.id.tv_menu_label);
      this.icon = itemView.findViewById(R.id.iv_menu_icon);
    }
  }

  private void initDrawer() {
    DrawerLayout dl = findViewById(R.id.dl_main);
    mDrawerToggle = new ActionBarDrawerToggle(this, dl, getToolbar(),
        android.R.string.ok, android.R.string.no);
    mDrawerToggle.setDrawerArrowDrawable(new DrawerArrowDrawable(this));
    dl.addDrawerListener(mDrawerToggle);

    mMenuList = findViewById(R.id.rv_left_menu);
    mMenuList.setItemAnimator(new DefaultItemAnimator());
    mMenuList.setLayoutManager(new LinearLayoutManager(this));

    List<String> titles = Arrays.asList("Settings", "Support", "License", "About");
    List<Integer> icons = Arrays.asList(
        R.attr.ic_settings,
        R.attr.ic_support,
        R.attr.ic_licenses,
        R.attr.ic_about
    );

    mMenuList.setAdapter(new RecyclerView.Adapter<MyHolder>() {
      @NonNull @Override
      public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu,
            parent, false);

        ThemeFramework.getInstance().bindView(MainActivity.this, item, R.layout.item_left_menu);
        return new MyHolder(item);
      }

      @Override
      public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.icon.setImageDrawable(ThemeFramework.getInstance().getThemeResources().getDrawable(icons.get(position)));
      }

      @Override public int getItemCount() {
        return titles.size();
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(R.string.settings)
        .setIcon(ThemeFramework.getInstance().getThemeResources().getDrawable(R.attr.ic_style))
        .setOnMenuItemClickListener(item -> {
          mSettingsDialog.show();
          return true;
        })
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    return super.onCreateOptionsMenu(menu);
  }

  public void onClickRegister(View view) {
    startActivity(new Intent(this, NextActivity.class));
  }
}