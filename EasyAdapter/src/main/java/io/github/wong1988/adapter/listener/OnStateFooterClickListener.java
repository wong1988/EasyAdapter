package io.github.wong1988.adapter.listener;

import io.github.wong1988.adapter.attr.LoadState;

/**
 * 列表加载状态脚布局的监听事件
 */
public interface OnStateFooterClickListener {
    void onClick(LoadState state);
}