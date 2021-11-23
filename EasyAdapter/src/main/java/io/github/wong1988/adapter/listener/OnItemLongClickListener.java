package io.github.wong1988.adapter.listener;

import android.view.View;

/**
 * 列表item的长按点击事件
 */
public interface OnItemLongClickListener<T> {
    void onLongClick(T t, int position, View view);
}