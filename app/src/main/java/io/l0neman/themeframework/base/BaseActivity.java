package io.l0neman.themeframework.base;

import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.l0neman.themeframework.R;
import io.l0neman.themeframework.lib.ThemeFramework;
import io.l0neman.themeframework.lib.ThemeResources;
import io.l0neman.themeframework.util.SystemUiUtils;

public abstract class BaseActivity extends AppCompatActivity {
  private Toolbar mToolbar;

  @Override public void setContentView(int layoutResID) {
    setSystemBarTheme();
    super.setContentView(layoutResID);
    initToolbar();
  }

  private void initToolbar() {
    mToolbar = findViewById(R.id.tb_main);
    setSupportActionBar(mToolbar);
  }

  protected Toolbar getToolbar() {
    return mToolbar;
  }

  public void setSystemBarTheme() {
    ThemeResources tr = ThemeFramework.getInstance().getThemeResources();
    SystemUiUtils.setStatueBarColor(this, tr.getColor(R.attr.appBackgroundColorDark));
    SystemUiUtils.setNavigationBarColor(this, tr.getColor(R.attr.appBackgroundColor));

    if (ThemeFramework.getInstance().getTheme() == R.style.AppTheme_Black) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        SystemUiUtils.setDarkStatusBar(this);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        SystemUiUtils.setDarkNavigationBar(this);
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        SystemUiUtils.setLightStatusBar(this);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        SystemUiUtils.setLightNavigationBar(this);
    }
  }
}
