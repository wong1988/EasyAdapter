package io.github.wong1988.adapter.attr;

/**
 * 加载状态
 */
public enum LoadState {

    LOAD_COMPLETE("加载完成"), LOADING("加载中"), LOAD_END("加载到底"), LOAD_ERROR("加载出错"), LOAD_NO_DATA("无数据");

    LoadState(String state) {

    }

}
