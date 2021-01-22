package io.l0neman.themeframework.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Arrays;
import java.util.List;

import io.l0neman.themeframework.R;
import io.l0neman.themeframework.lib.ThemeFramework;
import io.l0neman.themeframework.lib.ThemeResources;

public class SettingsDialog {
  private final AlertDialog mThemeDialog;

  public SettingsDialog(Context context) {
    View content = View.inflate(context, R.layout.dialog_theme_pick, null);

    ThemeFramework.getInstance().bindView((AppCompatActivity) context, content, R.layout.dialog_theme_pick);

    this.mThemeDialog = new AlertDialog.Builder(context)
        .setView(content)
        .create();

    Spinner spinner = content.findViewById(R.id.spinner_theme);
    Button ok = content.findViewById(R.id.btn_ok);

    ok.setOnClickListener(v -> {
      int selectedItemPosition = spinner.getSelectedItemPosition();
      switch (selectedItemPosition) {
      case 0:
        ThemeFramework.getInstance().switchTheme(R.style.AppTheme_Default);
        break;
      case 1:
        ThemeFramework.getInstance().switchTheme(R.style.AppTheme_Black);
        break;
      case 2:
        ThemeFramework.getInstance().switchTheme(R.style.AppTheme_White);
        break;
      }

      mThemeDialog.dismiss();
    });

    List<String> items = Arrays.asList("Default", "Black", "White");

    spinner.setAdapter(new BaseAdapter() {
      @Override public int getCount() {
        return items.size();
      }

      @Override public Object getItem(int position) {
        return items.get(position);
      }

      @Override public long getItemId(int position) {
        return position;
      }

      @Override public View getView(int position, View convertView, ViewGroup parent) {
        AppCompatTextView item = new AppCompatTextView(parent.getContext());
        int dp16 = dp2px(16);
        item.setPadding(dp16, dp16, dp16, dp16);
        item.setText(items.get(position));
        ThemeResources themeResources = ThemeFramework.getInstance().getThemeResources();

        item.setTextColor(themeResources.getColor(R.attr.appTextColor));
        item.setBackground(themeResources.getDrawable(R.attr.selectableItemBackground));
        return item;
      }
    });

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {}
    });
  }

  private static int dp2px(float dpValue) {
    return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
  }

  public void show() {
    mThemeDialog.show();
  }

  public void dismiss() {
    mThemeDialog.dismiss();
  }
}
