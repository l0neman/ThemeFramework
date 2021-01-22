package io.l0neman.themeframework.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import io.l0neman.themeframework.R;
import io.l0neman.themeframework.base.BaseActivity;
import io.l0neman.themeframework.dialog.SettingsDialog;
import io.l0neman.themeframework.lib.ThemeChangeListener;
import io.l0neman.themeframework.lib.ThemeFramework;

public class NextActivity extends BaseActivity implements ThemeChangeListener {
  private SettingsDialog mSettingsDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_next);

    ThemeFramework.getInstance().registerThemeChangedListener(this);

    View root = findViewById(R.id.content);
    ThemeFramework.getInstance().bind(this, root, R.layout.activity_next);
    ThemeFramework.getInstance().getAttributeAdapter(this, root, getToolbar(),
        attributeAdapter -> attributeAdapter.setOnSetViewListener((view, tr) -> {
              // 刷新 Toolbar 右上角主题菜单图标
              view.getMenu().getItem(0).setIcon(
                  attributeAdapter.getThemeResources().getDrawable(R.attr.ic_style)
              );

              getToolbar().setNavigationIcon(tr.getDrawable(R.attr.ic_back));
            }
        ));

    mSettingsDialog = new SettingsDialog(this);

    initToolbar();
    initViews();
  }

  private void initToolbar() {
    Toolbar toolbar = getToolbar();
    toolbar.setNavigationIcon(ThemeFramework.getInstance().getThemeResources().getDrawable(R.attr.ic_back));
    toolbar.setNavigationOnClickListener(v -> onBackPressed());
  }

  @Override public void onThemeChanged(int theme) {
    setSystemBarTheme();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ThemeFramework.getInstance().unregisterThemeChangedListener(this);
  }

  private interface FragmentFactory {
    Fragment create();
  }

  private void initViews() {
    TabLayout tabLayout = findViewById(R.id.tl_next);
    ViewPager2 viewPager = findViewById(R.id.vp_next);

    List<FragmentFactory> factories = Arrays.asList(
        MyFragment0::new, MyFragment1::new, MyFragment2::new
    );

    viewPager.setAdapter(new FragmentStateAdapter(this) {
      @NonNull @Override public Fragment createFragment(int position) {
        return factories.get(position).create();
      }

      @Override public int getItemCount() {
        return factories.size();
      }
    });

    List<String> titles = Arrays.asList("page0", "page1", "page2");

    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
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
}