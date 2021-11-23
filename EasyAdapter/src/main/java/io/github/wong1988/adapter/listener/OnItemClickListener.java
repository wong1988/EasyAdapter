package io.github.wong1988.adapter.listener;

import android.view.View;

/**
 * 列表item的点击事件
 */
public interface OnItemClickListener<T> {
    void onClick(T t, int position, View view);
}