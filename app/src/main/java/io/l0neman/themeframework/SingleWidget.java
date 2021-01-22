package io.l0neman.themeframework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import io.l0neman.themeframework.lib.ThemeFramework;
import io.l0neman.themeframework.lib.ThemeResources;

public class SingleWidget extends AppCompatActivity {

  private Spinner mSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_widget);
    ThemeFramework.getInstance().bind(this, findViewById(R.id.content), R.layout.activity_single_widget);

    initViews();
  }

  private void initViews() {
    mSpinner = findViewById(R.id.spinner_theme);
    List<String> items = Arrays.asList("Default", "Black", "White");

    mSpinner.setAdapter(new BaseAdapter() {
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
  }

  private static int dp2px(float dpValue) {
    return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
  }

  public void onTest(View view) {
    ThemeFramework tf = ThemeFramework.getInstance();
    if (tf.getTheme() == R.style.AppTheme_Black)
      tf.switchTheme(R.style.AppTheme_White);
    else
      tf.switchTheme(R.style.AppTheme_Black);

    ThemeResources themeResources = ThemeFramework.getInstance().getThemeResources();
    ((TextView)mSpinner.getSelectedView()).setTextColor(themeResources.getColor(R.attr.appTextColor));
//    mSpinner.setPopupBackgroundDrawable(tf.getThemeResources().getDrawable(R.attr.appBackgroundColor));
  }
}