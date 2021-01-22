package io.l0neman.themeframework.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.l0neman.themeframework.R;
import io.l0neman.themeframework.lib.ThemeFramework;

public class MyFragment0 extends Fragment {
  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View root = View.inflate(getContext(), R.layout.fragment_next_page0, null);
    root.findViewById(R.id.btn_go_back).setOnClickListener(v -> getActivity().onBackPressed());

    ThemeFramework.getInstance().bind(this, root, R.layout.fragment_next_page0);
    return root;
  }

  private static class ItemViewHolder {
    View mItemView;
    TextView mTitle;

    public ItemViewHolder(View mItemView) {
      this.mItemView = mItemView;

      mTitle = mItemView.findViewById(R.id.tv_item);
    }
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    ListView listView = view.findViewById(R.id.lv_page0);

    List<Integer> items = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

    listView.setAdapter(new BaseAdapter() {
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
        ItemViewHolder viewHolder;
        if (convertView != null)
          viewHolder = (ItemViewHolder) convertView.getTag();
        else {
          View item = LayoutInflater.from(getContext()).inflate(R.layout.item_page0, parent, false);
          ThemeFramework.getInstance().bindView(MyFragment0.this, item, R.layout.item_page0);

          viewHolder = new ItemViewHolder(item);
          convertView = viewHolder.mItemView;
          convertView.setTag(viewHolder);
        }

        viewHolder.mTitle.setText(String.format(Locale.US, "item %d", items.get(position)));
        return viewHolder.mItemView;
      }
    });
  }
}