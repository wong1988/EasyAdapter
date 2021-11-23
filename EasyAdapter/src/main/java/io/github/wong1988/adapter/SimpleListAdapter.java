package io.github.wong1988.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.wong1988.adapter.holder.RecyclerViewHolder;

/**
 * 单布局列表的简单适配器
 */
public abstract class SimpleListAdapter<T> extends BaseListAdapter<T> {

    // item的布局资源
    private final int mLayoutResId;

    /**
     * 初始化无需数据源,默认支持上拉加载
     */
    public SimpleListAdapter(Context context, int layoutResId) {
        super(context, true);
        mLayoutResId = layoutResId;
    }

    /**
     * 初始化无需数据源
     */
    public SimpleListAdapter(Context context, int layoutResId, boolean enableLoadMore) {
        super(context, enableLoadMore);
        mLayoutResId = layoutResId;
    }

    /**
     * 初始化需数据源，默认支持上拉加载
     */
    public SimpleListAdapter(Context context, int layoutResId, List<T> list) {
        this(context, layoutResId, true);
        addData(list);
    }

    /**
     * 初始化需数据源
     */
    public SimpleListAdapter(Context context, int layoutResId, boolean enableLoadMore, List<T> list) {
        this(context, layoutResId, enableLoadMore);
        addData(list);
    }

    @Override
    public final int getItemViewsType(int position) {
        return 1;
    }

    @Override
    public final RecyclerViewHolder onCreateViewHolders(ViewGroup parent, int viewType) {
        View itemView = getInflater().inflate(mLayoutResId, parent, false);
        return new RecyclerViewHolder(itemView);
    }
}
