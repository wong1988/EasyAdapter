package io.github.adapter.example.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import io.github.adapter.example.R;
import io.github.wong1988.adapter.SimpleListAdapter;
import io.github.wong1988.adapter.holder.RecyclerViewHolder;


public class LinearVerticalAdapter extends SimpleListAdapter<String> {

    private static List<String> list;

    static {

        list = Arrays.asList(
                "我是第1条数据",
                "我是第2条数据",
                "我是第3条数据",
                "我是第4条数据",
                "我是第5条数据",
                "我是第6条数据",
                "我是第7条数据",
                "我是第8条数据",
                "我是第9条数据",
                "我是第10条数据",
                "我是第n条数据"
        );
    }

    public LinearVerticalAdapter(Context context) {
        super(context, R.layout.item_vertical, false, list);
    }

    public void addHeader() {
        TextView header = new TextView(getAttachContext());
        header.setBackgroundColor(getAttachContext().getResources().getColor(android.R.color.holo_red_dark));
        header.setText("头布局" + System.currentTimeMillis());
        addHeaderView(header, LinearLayout.HORIZONTAL);
    }

    public void addFooter() {
        TextView footer = new TextView(getAttachContext());
        footer.setText("脚布局" + System.currentTimeMillis());
        footer.setBackgroundColor(getAttachContext().getResources().getColor(android.R.color.holo_red_dark));
        addFooterView(footer);
    }

    @Override
    public void onBindViewHolders(RecyclerViewHolder holder, int viewType, String s, int position) {
        holder.getTextView(R.id.tv).setText(s);
    }

    // 此方法LinearLayoutManger下不生效
    @Override
    public int getSpanSize(String s, int position) {
        return position == 3 ? 2 : 1;
    }
}
