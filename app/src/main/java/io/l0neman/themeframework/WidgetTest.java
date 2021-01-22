package io.l0neman.themeframework;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.l0neman.themeframework.lib.ThemeFramework;

public class WidgetTest extends AppCompatActivity {

  private static final String TAG = "WidgetTest";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_widget_test);
    ThemeFramework.getInstance().bind(this, findViewById(R.id.ll_content), R.layout.activity_widget_test);

    initViews();
  }

  private static final class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView mTitle;

    public ItemViewHolder(@NonNull View itemView) {
      super(itemView);

      mTitle = itemView.findViewById(R.id.tv_item);
    }
  }

  private AlertDialog mDialog;

  private void initViews() {
    SwitchCompat theme = findViewById(R.id.sw_theme);
    FloatingActionButton fab = findViewById(R.id.fab_test);

    fab.setOnClickListener(v -> {
      if (mDialog == null) {
        View contentView = View.inflate(WidgetTest.this, R.layout.item_page1, null);
        ThemeFramework.getInstance().bindView(WidgetTest.this, contentView, R.layout.item_page1);

        mDialog = new AlertDialog.Builder(WidgetTest.this)
            .setView(contentView)
            .create();
      }

      mDialog.show();
    });

    theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
      ThemeFramework.getInstance().switchTheme(isChecked ?
          R.style.AppTheme_Black : R.style.AppTheme_White);
    });

    // init recyclerView

    RecyclerView recyclerView = findViewById(R.id.rv_list);

    List<Integer> items = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
    recyclerView.setAdapter(new RecyclerView.Adapter<ItemViewHolder>() {
      @NonNull @Override
      public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(WidgetTest.this).inflate(R.layout.item_page1, parent, false);
        Log.d(TAG, "onCreateViewHolder");

        ThemeFramework.getInstance().bindView(WidgetTest.this, item, R.layout.item_page1);

        return new ItemViewHolder(item);
      }

      @Override
      public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.mTitle.setText(String.format(Locale.US, "item %d", items.get(position)));
      }

      @Override public int getItemCount() {
        return items.size();
      }
    });
  }

  private ColorStateList get(int color) {
    return ColorStateList.valueOf(color);
  }
}