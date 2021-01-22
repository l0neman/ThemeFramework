package io.l0neman.themeframework.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.l0neman.themeframework.R;
import io.l0neman.themeframework.lib.ThemeAttributeAdapter;
import io.l0neman.themeframework.lib.ThemeFramework;
import io.l0neman.themeframework.lib.ThemeResources;

public class MyFragment1 extends Fragment {
  @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                               @Nullable ViewGroup container,
                                               @Nullable Bundle savedInstanceState) {

    View root = View.inflate(getContext(), R.layout.fragment_next_page1, null);
    ThemeFramework.getInstance().bind(this, root, R.layout.fragment_next_page1);
    return root;
  }

  private static final class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView mTitle;

    public ItemViewHolder(@NonNull View itemView) {
      super(itemView);

      mTitle = itemView.findViewById(R.id.tv_item);
    }
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    RecyclerView recyclerView = view.findViewById(R.id.tv_page1);

    List<Integer> items = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    recyclerView.setAdapter(new RecyclerView.Adapter<ItemViewHolder>() {
      @NonNull @Override
      public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_page1, parent, false);
        ThemeFramework.getInstance().bindView(MyFragment1.this, item, R.layout.item_page1);
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
}