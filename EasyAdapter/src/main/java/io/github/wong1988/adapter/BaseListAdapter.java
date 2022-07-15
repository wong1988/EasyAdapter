package io.github.wong1988.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.adapter.attr.LoadState;
import io.github.wong1988.adapter.holder.RecyclerViewHolder;
import io.github.wong1988.adapter.listener.OnItemClickListener;
import io.github.wong1988.adapter.listener.OnItemLongClickListener;
import io.github.wong1988.adapter.listener.OnLoadMoreListener;
import io.github.wong1988.adapter.listener.OnScrollListener;
import io.github.wong1988.adapter.listener.OnStateFooterClickListener;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    // 数据源
    private final List<T> mData;
    // Attach的RecyclerView
    private RecyclerView mRecyclerView;
    // 上下文对象
    private final Context mContext;
    // 布局填充器
    private final LayoutInflater mInflater;
    // item的点击事件
    private OnItemClickListener mListener;
    // item的长按点击事件
    private OnItemLongClickListener mLongListener;
    // 加载更多的监听
    private OnLoadMoreListener mLoadMoreListener;
    // 滚动的监听
    private OnScrollListener mScrollListener;
    // header footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    // 是否支持上拉加载
    private final boolean mEnableLoadMore;
    // 当前加载状态，默认为加载完成
    private LoadState loadState = LoadState.LOAD_COMPLETE;
    // 头布局
    private final int TYPE_HEADER = Integer.MAX_VALUE - 2;
    // 脚布局
    private final int TYPE_FOOTER = Integer.MAX_VALUE - 1;
    // 加载状态脚布局
    private final int TYPE_LOAD_STATE_FOOTER = Integer.MAX_VALUE;
    // loading资源
    private int mLoadingRes = R.raw.wong_state_loading;
    private String mLoadingText = "加载中";
    // 加载到底资源
    private String mEndText = "END";
    // 加载出错资源
    private int mErrorRes = R.raw.wong_state_error;
    private String mErrorText = "";
    private boolean mErrorTextSet;
    // 无数据资源
    private int mEmptyRes = R.raw.wong_state_no_data;
    private String mEmptyText = "暂无数据";
    // 加载状态脚布局的点击事件
    private OnStateFooterClickListener mStateListener;
    // params
    private LinearLayout.LayoutParams wrapParams;
    private final LinearLayout.LayoutParams noParams;
    private LinearLayout.LayoutParams emptyParams;
    //用来标记是否正在向上滑动
    private boolean mIsSlidingUpward = false;
    //用来标记是否正在向右滑动
    private boolean mIsSlidingRight = false;
    // 脚布局状态Layout
    private int mStateFooterLayout = R.layout.wong_recycle_item_foot_v;
    // 完成状态是否显示
    private boolean mIsShowComplete = false;

    /**
     * 构造方法，默认支持上拉加载
     */
    public BaseListAdapter(Context context) {
        this(context, true);
    }

    /**
     * 构造方法
     */
    public BaseListAdapter(Context context, boolean enableLoadMore) {
        mData = new ArrayList<>();
        this.mContext = context;
        this.mEnableLoadMore = enableLoadMore;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wrapParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        emptyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT);
        noParams = new LinearLayout.LayoutParams(0, 0);
    }

    // ******** 获取item的数量 ********
    @Override
    public final int getItemCount() {
        // 头布局+数据源+脚布局+状态布局
        return getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() + getLoadStateViewCount();
    }

    // ******** 获取item的type类型 ********
    @Override
    public final int getItemViewType(int position) {

        if (position == 0 && getHeaderLayoutCount() != 0) {
            return TYPE_HEADER;
        }

        // 真正数据源已获取完毕
        if (position == mData.size() + getHeaderLayoutCount()) {
            return getFooterLayoutCount() == 0 ? TYPE_LOAD_STATE_FOOTER : TYPE_FOOTER;
        }


        // 已经获取完脚布局
        if (position == mData.size() + getHeaderLayoutCount() + getFooterLayoutCount()) {
            return TYPE_LOAD_STATE_FOOTER;
        }

        // 返回数据源对应的真实position
        return getItemViewsType(position - getHeaderLayoutCount());
    }

    // 多布局的ViewType
    public abstract int getItemViewsType(int position);

    // ******** 创建Holder ********
    @NonNull
    @Override
    public final RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_LOAD_STATE_FOOTER) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            int orientation = RecyclerView.VERTICAL;
            if (manager instanceof LinearLayoutManager) {
                orientation = ((LinearLayoutManager) manager).getOrientation();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                orientation = ((StaggeredGridLayoutManager) manager).getOrientation();
            }
            if (orientation == RecyclerView.HORIZONTAL) {
                // 使用对应的脚布局文件
                mStateFooterLayout = R.layout.wong_recycle_item_foot_h;
                wrapParams = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
                emptyParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            } else {
                mStateFooterLayout = R.layout.wong_recycle_item_foot_v;
                wrapParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                emptyParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            }
            View view = mInflater.inflate(mStateFooterLayout, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStateListener != null)
                        mStateListener.onClick(loadState);
                }
            });
            return new FootLoadStateViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            ViewParent headerLayoutVp = mHeaderLayout.getParent();
            if (headerLayoutVp instanceof ViewGroup)
                ((ViewGroup) headerLayoutVp).removeView(mHeaderLayout);
            return new HeaderViewHolder(mHeaderLayout);
        } else if (viewType == TYPE_FOOTER) {
            ViewParent footerLayoutVp = mFooterLayout.getParent();
            if (footerLayoutVp instanceof ViewGroup)
                ((ViewGroup) footerLayoutVp).removeView(mFooterLayout);
            return new FooterViewHolder(mFooterLayout);
        } else {
            // 用户自定义的Holder
            RecyclerViewHolder viewHolder = onCreateViewHolders(parent, viewType);
            viewHolder.itemView.setOnClickListener(this);
            viewHolder.itemView.setOnLongClickListener(this);
            return viewHolder;
        }
    }

    // 创建多布局的ViewHolder
    public abstract RecyclerViewHolder onCreateViewHolders(ViewGroup parent, int viewType);

    // ******** 绑定显示数据 ********
    @SuppressLint("ResourceType")
    @Override
    public final void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if (holder instanceof FootLoadStateViewHolder) {
            FootLoadStateViewHolder footViewHolder = (FootLoadStateViewHolder) holder;
            // 根布局
            FrameLayout layout = footViewHolder.getFrameLayout(R.id.footer_layout);
            // 加载状态的lottieView
            LottieAnimationView loadingView = footViewHolder.getView(R.id.lottie_loading);
            // 加载出错的lottieView
            LottieAnimationView errorView = footViewHolder.getView(R.id.lottie_error);
            // 加载空数据的lottieView
            LottieAnimationView emptyView = footViewHolder.getView(R.id.lottie_none);
            switch (loadState) {
                case LOADING: // 正在加载
                    layout.setLayoutParams(wrapParams);
                    footViewHolder.setVisibility(R.id.loading_layout, View.VISIBLE);
                    footViewHolder.setVisibility(R.id.end_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.error_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.no_data_layout, View.GONE);
                    footViewHolder.setText(R.id.loading_tv, mLoadingText);
                    String resourceTypeName = "";
                    try {
                        resourceTypeName = mContext.getResources().getResourceTypeName(mLoadingRes);
                    } catch (Exception ignored) {
                    }
                    if (resourceTypeName.equals("mipmap") || resourceTypeName.equals("drawable")) {
                        loadingView.setImageResource(mLoadingRes);
                        loadingView.pauseAnimation();
                    } else if (resourceTypeName.equals("raw")) {
                        loadingView.setAnimation(mLoadingRes);
                        loadingView.playAnimation();
                    } else {
                        loadingView.setImageResource(0);
                        loadingView.pauseAnimation();
                    }
                    emptyView.pauseAnimation();
                    errorView.pauseAnimation();
                    break;
                case LOAD_COMPLETE: // 加载完成
                    layout.setLayoutParams(mIsShowComplete ? wrapParams : noParams);
                    footViewHolder.setVisibility(R.id.loading_layout, View.INVISIBLE);
                    footViewHolder.setVisibility(R.id.end_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.error_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.no_data_layout, View.GONE);
                    loadingView.pauseAnimation();
                    emptyView.pauseAnimation();
                    errorView.pauseAnimation();
                    break;
                case LOAD_END: // 加载到底
                    layout.setLayoutParams(wrapParams);
                    footViewHolder.setVisibility(R.id.loading_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.end_layout, View.VISIBLE);
                    footViewHolder.setVisibility(R.id.error_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.no_data_layout, View.GONE);
                    footViewHolder.setText(R.id.end_tv, mEndText);
                    loadingView.pauseAnimation();
                    emptyView.pauseAnimation();
                    errorView.pauseAnimation();
                    break;
                case LOAD_ERROR: // 加载出错
                    layout.setLayoutParams(wrapParams);
                    footViewHolder.setVisibility(R.id.loading_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.end_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.error_layout, View.VISIBLE);
                    footViewHolder.setVisibility(R.id.no_data_layout, View.GONE);
                    if (mErrorTextSet)
                        footViewHolder.setText(R.id.error_tv, mErrorText);
                    else {
                        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                        if (manager instanceof LinearLayoutManager && ((LinearLayoutManager) manager).getOrientation() == RecyclerView.HORIZONTAL)
                            footViewHolder.setText(R.id.error_tv, "查看更多");
                        else
                            footViewHolder.setText(R.id.error_tv, "载入失败，点击重试");
                    }
                    String resourceTypeName2 = "";
                    try {
                        resourceTypeName2 = mContext.getResources().getResourceTypeName(mErrorRes);
                    } catch (Exception ignored) {
                    }
                    if (resourceTypeName2.equals("mipmap") || resourceTypeName2.equals("drawable")) {
                        errorView.setImageResource(mErrorRes);
                        errorView.pauseAnimation();
                    } else if (resourceTypeName2.equals("raw")) {
                        errorView.setAnimation(mErrorRes);
                        errorView.playAnimation();
                    } else {
                        errorView.setImageResource(0);
                        errorView.pauseAnimation();
                    }
                    loadingView.pauseAnimation();
                    emptyView.pauseAnimation();
                    break;
                case LOAD_NO_DATA:
                    layout.setLayoutParams(emptyParams);
                    footViewHolder.setVisibility(R.id.loading_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.end_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.error_layout, View.GONE);
                    footViewHolder.setVisibility(R.id.no_data_layout, View.VISIBLE);
                    footViewHolder.setText(R.id.none_tv, mEmptyText);
                    String resourceTypeName3 = "";
                    try {
                        resourceTypeName3 = mContext.getResources().getResourceTypeName(mEmptyRes);
                    } catch (Exception ignored) {
                    }
                    if (resourceTypeName3.equals("mipmap") || resourceTypeName3.equals("drawable")) {
                        emptyView.setImageResource(mEmptyRes);
                        emptyView.pauseAnimation();
                    } else if (resourceTypeName3.equals("raw")) {
                        emptyView.setAnimation(mEmptyRes);
                        emptyView.playAnimation();
                    } else {
                        emptyView.setImageResource(0);
                        emptyView.pauseAnimation();
                    }
                    loadingView.pauseAnimation();
                    errorView.pauseAnimation();
                    break;
            }
        } else if (holder instanceof FooterViewHolder) {

        } else if (holder instanceof HeaderViewHolder) {

        } else {
            // 需要子类去实现 具体操作
            onBindViewHolders(holder, getItemViewsType(position), mData.get(position - getHeaderLayoutCount()), position - getHeaderLayoutCount());
        }
    }

    // 绑定数据
    public abstract void onBindViewHolders(RecyclerViewHolder holder, int viewType, T t, int position);

    /**
     * 移除所有头布局
     */
    public final void removeAllHeaders() {
        if (mHeaderLayout != null && mHeaderLayout.getChildCount() > 0) {
            mHeaderLayout.removeAllViews();
            notifyItemRangeRemoved(0, 1);
        }
    }

    /**
     * 移除指定头布局
     */
    public final void removeHeader(View removeView) {
        if (mHeaderLayout != null) {
            mHeaderLayout.removeView(removeView);

            if (mHeaderLayout.getChildCount() == 0)
                notifyItemRangeRemoved(0, 1);
        }
    }

    /**
     * 移除指定位置的头布局
     */
    public final void removeHeader(int index) {
        if (mHeaderLayout != null && index >= 0 && mHeaderLayout.getChildCount() > index) {
            mHeaderLayout.removeViewAt(index);

            if (mHeaderLayout.getChildCount() == 0)
                notifyItemRangeRemoved(0, 1);
        }
    }

    /**
     * 添加头布局，默认垂直摆放，顺序为自动排到末尾
     */
    public final void addHeaderView(View header) {
        addHeaderView(header, -1, LinearLayout.VERTICAL);
    }

    /**
     * 添加头布局，顺序为自动排到末尾
     */
    public final void addHeaderView(View header, int orientation) {
        addHeaderView(header, -1, orientation);
    }

    /**
     * 添加头布局
     * 注意：添加同一个头部视图对象，只进行替换，并不新增
     */
    public final void addHeaderView(View header, final int index, int orientation) {

        if (mHeaderWidth == null || mHeaderHeight == null) {
            mHeaderWidth = orientation == LinearLayout.VERTICAL ? MATCH_PARENT : WRAP_CONTENT;
            mHeaderHeight = orientation == LinearLayout.VERTICAL ? WRAP_CONTENT : MATCH_PARENT;
        }

        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
        }

        ViewGroup.LayoutParams layoutParams = mHeaderLayout.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(mHeaderWidth, mHeaderHeight);
        } else {
            layoutParams.width = mHeaderWidth;
            layoutParams.height = mHeaderHeight;
        }

        mHeaderLayout.setLayoutParams(layoutParams);
        mHeaderLayout.setOrientation(orientation);

        final int childCount = mHeaderLayout.getChildCount();
        int mIndex = index;

        // 传入index不符合规范自动插入到头布局最后的位置
        if (index < 0 || index > childCount) {
            mIndex = childCount;
        }

        for (int i = 0; i < childCount; i++) {
            View childAt = mHeaderLayout.getChildAt(i);

            if (childAt == header) {
                mHeaderLayout.removeView(childAt);
                mIndex = i;
                break;
            }
        }

        mHeaderLayout.addView(header, mIndex);

        if (childCount == 0 && mHeaderLayout.getChildCount() == 1)
            // 通知适配器刷新第一条数据
            notifyItemInserted(0);
    }

    // 头布局宽高
    private Integer mHeaderWidth;
    private Integer mHeaderHeight;

    /**
     * 设置头布局的宽高
     * 流式布局特殊：头布局设计到合并充满，部分情况下 宽高可能有一个值会不生效
     */
    public final void setHeaderLayoutParams(int width, int height) {

        this.mHeaderWidth = width;
        this.mHeaderHeight = height;

        if (mHeaderLayout != null) {
            ViewGroup.LayoutParams layoutParams = mHeaderLayout.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(width, height);
            } else {
                layoutParams.width = width;
                layoutParams.height = height;
            }
            mHeaderLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * 移除所有脚布局
     */
    public final void removeAllFooters() {
        if (mFooterLayout != null && mFooterLayout.getChildCount() > 0) {
            mFooterLayout.removeAllViews();
            notifyItemRangeRemoved(getFooterPosition(), 1);
        }
    }

    /**
     * 移除指定脚布局
     */
    public final void removeFooter(View removeView) {
        if (mFooterLayout != null) {
            mFooterLayout.removeView(removeView);

            if (mFooterLayout.getChildCount() == 0)
                notifyItemRangeRemoved(getFooterPosition(), 1);
        }
    }

    /**
     * 移除指定位置的脚布局
     */
    public final void removeFooter(int index) {
        if (mFooterLayout != null && index >= 0 && mFooterLayout.getChildCount() > index) {
            mFooterLayout.removeViewAt(index);

            if (mFooterLayout.getChildCount() == 0)
                notifyItemRangeRemoved(getFooterPosition(), 1);
        }
    }

    /**
     * 添加脚布局，默认垂直摆放，顺序为自动排到末尾
     */
    public final void addFooterView(View footer) {
        addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    /**
     * 添加脚布局，顺序为自动排到末尾
     */
    public final void addFooterView(View footer, int orientation) {
        addFooterView(footer, -1, orientation);
    }

    /**
     * 添加脚布局
     */
    public final void addFooterView(View footer, int index, int orientation) {

        if (mFooterWidth == null || mFooterHeight == null) {
            mFooterWidth = orientation == LinearLayout.VERTICAL ? MATCH_PARENT : WRAP_CONTENT;
            mFooterHeight = orientation == LinearLayout.VERTICAL ? WRAP_CONTENT : MATCH_PARENT;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
        }

        ViewGroup.LayoutParams layoutParams = mFooterLayout.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(mFooterWidth, mFooterHeight);
        } else {
            layoutParams.width = mFooterWidth;
            layoutParams.height = mFooterHeight;
        }

        mFooterLayout.setLayoutParams(layoutParams);
        mFooterLayout.setOrientation(orientation);

        final int childCount = mFooterLayout.getChildCount();

        if (index < 0 || index > childCount) {
            index = childCount;
        }

        for (int i = 0; i < childCount; i++) {
            View childAt = mFooterLayout.getChildAt(i);

            if (childAt == footer) {
                mFooterLayout.removeView(childAt);
                index = i;
                break;
            }
        }

        mFooterLayout.addView(footer, index);


        if (childCount == 0 && mFooterLayout.getChildCount() == 1)
            // 通知适配器刷新脚布局数据
            notifyItemInserted(getFooterPosition());
    }

    // 脚布局宽高
    private Integer mFooterWidth;
    private Integer mFooterHeight;

    /**
     * 设置脚布局的宽高
     * 流式布局特殊：脚布局设计到合并充满，部分情况下 宽高可能有一个值会不生效
     */
    public final void setFooterLayoutParams(int width, int height) {

        this.mFooterWidth = width;
        this.mFooterHeight = height;

        if (mFooterLayout != null) {
            ViewGroup.LayoutParams layoutParams = mFooterLayout.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(width, height);
            } else {
                layoutParams.width = mFooterWidth;
                layoutParams.height = mFooterHeight;
            }
            mFooterLayout.setLayoutParams(layoutParams);
        }
    }

    // 获取脚布局的刷新位置
    private int getFooterPosition() {
        int position = getItemCount() - getFooterLayoutCount() - getLoadStateViewCount();
        return Math.max(position, 0);
    }

    // 获取用户自定义头布局数量
    private int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    // 是否绘制了头布局
    public boolean isCanvasHeader() {
        return mHeaderLayout != null && mHeaderLayout.getChildCount() != 0;
    }

    // 获取用户自定义脚布局数量
    private int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    // 获取加载状态的脚布局数量
    private int getLoadStateViewCount() {
        return 1;
    }

    // 表格布局 头、脚布局占一行 添加加载更多监听
    @CallSuper
    @Override
    public final void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;

        mIsSlidingUpward = false;
        mIsSlidingRight = false;

        // 布局管理器
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 动态更新完成时的脚布局
                        if (loadState == LoadState.LOAD_COMPLETE) {

                            if (manager == null)
                                return;

                            if (!mEnableLoadMore) {
                                mIsShowComplete = false;
                                return;
                            }

                            // 当前屏幕所看到的子项个数
                            int visibleItemCount = manager.getChildCount();

                            if (visibleItemCount == 0) {
                                // 还未构建
                                mIsShowComplete = false;
                                return;
                            }

                            if (visibleItemCount < manager.getItemCount()) {
                                // 超出一屏幕
                                mIsShowComplete = true;
                            } else {
                                // 未超出一屏幕
                                mIsShowComplete = false;
                            }
                        }
                    }
                });

        // 监听recyclerView滚动
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mScrollListener != null)
                    mScrollListener.onScrollStateChanged(recyclerView, newState);

                // 不支持上拉加载不监听了
                if (!mEnableLoadMore)
                    return;

                // 加载中、加载出错、无数据以及加载到底状态不监听
                if (loadState != LoadState.LOAD_COMPLETE)
                    return;

                // 变量 最后一个可见的position
                int lastItemPosition = -1;

                if (manager == null)
                    return;

                // 开始滚动（SCROLL_STATE_FLING），正在滚动(SCROLL_STATE_TOUCH_SCROLL), 已经停止（SCROLL_STATE_IDLE）

                // 获取最后一个显示的itemPosition
                if (manager instanceof GridLayoutManager) {
                    // 通过LayoutManager找到当前显示的最后的item的position
                    lastItemPosition = ((GridLayoutManager) manager).findLastVisibleItemPosition();
                } else if (manager instanceof LinearLayoutManager) {
                    lastItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    // 因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                    // 得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
                    ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(lastPositions);
                    lastItemPosition = findMaxByStaggeredGrid(lastPositions);
                }

                int itemCount = manager.getItemCount();

                // 判断是否滑动到了非加载状态的最后一个item，并且是向上滑动
                if (lastItemPosition >= (itemCount - 1 - getLoadStateViewCount()) && (mIsSlidingUpward || mIsSlidingRight) && mLoadMoreListener != null && loadState == LoadState.LOAD_COMPLETE) {
                    setLoadState(LoadState.LOADING);
                    //加载更多
                    mLoadMoreListener.onLoadMore();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                mIsSlidingUpward = dy > 0;
                // 大于0表示正在向右滑动，小于等于0表示停止或向左滑动
                mIsSlidingRight = dx > 0;

                if (mScrollListener != null)
                    mScrollListener.onScrolled(recyclerView, dx, dy);
            }
        });

        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    int type = getItemViewType(position);

                    //  头脚、状态布局 占满一行
                    if (type == TYPE_HEADER || type == TYPE_FOOTER || type == TYPE_LOAD_STATE_FOOTER) {
                        return gridManager.getSpanCount();
                    }

                    // 其他占据本身1个位置
                    int realPosition = position - getHeaderLayoutCount();
                    return BaseListAdapter.this.getSpanSize(getAttachData(realPosition), realPosition);
                }
            });
        }
    }

    /**
     * 请不要在LayoutManger里进行设置，重写此方法进行设置
     */
    public int getSpanSize(T t, int position) {
        return 1;
    }

    /**
     * 流式布局 可重写此方法，对item占用进行调整
     */
    public void onViewAttachedToWindow2(@NonNull RecyclerViewHolder holder, T t, int position) {

    }

    private int findMaxByStaggeredGrid(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    // 流式布局 头、脚布局占一行
    @Override
    public final void onViewAttachedToWindow(@NonNull RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == TYPE_HEADER || type == TYPE_FOOTER || type == TYPE_LOAD_STATE_FOOTER) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) params;
                layoutParams.setFullSpan(true);
            }
        } else {
            int position = mRecyclerView.getChildAdapterPosition(holder.itemView);
            T t = mData.get(position - getHeaderLayoutCount());
            onViewAttachedToWindow2(holder, t, position - getHeaderLayoutCount());
        }
    }

    @Override
    public final void onClick(View v) {

        int position = mRecyclerView.getChildAdapterPosition(v);

        T t = mData.get(position - getHeaderLayoutCount());
        if (mListener != null) {
            mListener.onClick(t, position - getHeaderLayoutCount(), v);
        }
    }

    @Override
    public final boolean onLongClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        T t = mData.get(position - getHeaderLayoutCount());
        if (mLongListener != null) {
            mLongListener.onLongClick(t, position - getHeaderLayoutCount(), view);
        }
        return mLongListener != null;
    }

    /**
     * 添加数据源
     */
    public final void addData(List<T> data) {

        if (data == null)
            return;

        // 插入的真正position
        int start = mData.size() + getHeaderLayoutCount();

        mData.addAll(data);
        // 通知刷新，刷新位置为当前插入数据源的真正位置，个数为追加的数据源具体个数,脚布局，头布局不参与刷新，因为是动态添加
        notifyItemRangeInserted(start, data.size());
    }

    /**
     * 添加数据
     */
    public final void addData(T data) {

        int start = mData.size() + getHeaderLayoutCount();

        mData.add(data);
        notifyItemInserted(start);
        // 因为插入的最后一位，会自动bind，
        // 且脚布局为内部控制，不对外提供position，所以不用调用刷新
    }

    /**
     * 添加数据
     */
    public final void addData(int position, T data) {

        if (position < 0)
            position = 0;

        if (position >= mData.size()) {
            addData(data);
            return;
        }

        int start = getHeaderLayoutCount() + position;

        mData.add(position, data);
        notifyItemInserted(start);
        // 刷新的位置为数据源真正的位置，
        // 刷新个数为总长度-插入的下标位置
        notifyItemRangeChanged(start, mData.size() - position);
    }

    /**
     * 清空数据源
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void clearAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除指定位置数据
     */
    public final void remove(int position) {
        mData.remove(position);
        // 真正移除数据源的位置
        notifyItemRemoved(position + getHeaderLayoutCount());
        // 通知刷新，刷新位置为当前改动数据源的真正位置，个数为用户传递的数据源具体个数,脚布局，头布局不参与刷新，因为是动态添加
        notifyItemRangeChanged(position + getHeaderLayoutCount(), mData.size() - position);
    }

    /**
     * 更新指定位置的数据
     */
    public final void update(int position, T t) {
        mData.set(position, t);
        // 通知刷新，刷新位置为当前改动数据源的真正位置
        notifyItemRangeChanged(position + getHeaderLayoutCount(), 1);
    }

    /**
     * 获取数据源的方法,尽量不要使用此方法操作数据
     * 如内部提供的方法不能够满足，再使用此方式获取数据源进行操作
     */
    public final List<T> getAttachData() {
        return mData;
    }

    /**
     * 获取指定位置的实体类，请注意判空处理
     */
    public final T getAttachData(int position) {
        return position < 0 || position + 1 > mData.size() ? null : mData.get(position);
    }

    /**
     * 获取数据源的长度
     */
    public final int getAttachDataSize() {
        return mData.size();
    }

    /**
     * 获取上下文对象
     */
    public final Context getAttachContext() {
        return mContext;
    }

    /**
     * 获取RecyclerView
     */
    public final RecyclerView getAttachRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 获取布局填充器
     */
    public final LayoutInflater getInflater() {
        return mInflater;
    }

    /**
     * 获取是否支持上拉加载
     */
    public boolean isEnableLoadMore() {
        return mEnableLoadMore;
    }

    /**
     * 设置Item的点击事件
     */
    public final void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mListener = listener;
    }

    /**
     * 设置Item的长按点击事件
     */
    public final void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.mLongListener = listener;
    }

    /**
     * 设置加载更多事件
     */
    public final void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    /**
     * 设置滚动监听事件
     */
    public final void setOnScrollListener(OnScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    /**
     * 设置加载错误的监听的事件
     */
    public final void setOnStateFooterClickListener(OnStateFooterClickListener stateListener) {
        this.mStateListener = stateListener;
    }

    /**
     * 设置加载状态
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setLoadState(LoadState loadState) {
        this.loadState = loadState;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置加载状态的资源，可以是drawable/mipmap下的图片资源，也可以是raw下的json动画资源
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setLoadingRes(int loadingRes) {
        this.mLoadingRes = loadingRes;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置loading状态文字描述
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setLoadingText(String text) {
        this.mLoadingText = TextUtils.isEmpty(text) ? "" : text;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置加载到底状态文字描述
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setEndText(String text) {
        this.mEndText = TextUtils.isEmpty(text) ? "" : text;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置加载出错的资源，可以是drawable/mipmap下的图片资源，也可以是raw下的json动画资源
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setErrorRes(int errorRes) {
        this.mErrorRes = errorRes;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置error状态文字描述
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setErrorText(String text) {
        this.mErrorText = TextUtils.isEmpty(text) ? "" : text;
        this.mErrorTextSet = true;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置空数据的资源，可以是drawable/mipmap下的图片资源，也可以是raw下的json动画资源
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setEmptyRes(int emptyRes) {
        this.mEmptyRes = emptyRes;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 设置空数据状态文字描述
     */
    @SuppressLint("NotifyDataSetChanged")
    public final void setEmptyText(String text) {
        this.mEmptyText = TextUtils.isEmpty(text) ? "" : text;
        // 刷新指定位置
        notifyItemRangeChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount(), 1);
    }

    /**
     * 移除所有分割线
     */
    public final void removeAllItemDecoration() {
        RecyclerView recyclerView = getAttachRecyclerView();
        if (recyclerView == null)
            return;

        for (int i = 0; i < recyclerView.getItemDecorationCount(); i++) {
            recyclerView.removeItemDecorationAt(i);
            i--;
        }
    }

    public final int getRealPosition(int position) {
        return position + getHeaderLayoutCount();
    }

    private static class HeaderViewHolder extends RecyclerViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FooterViewHolder extends RecyclerViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FootLoadStateViewHolder extends RecyclerViewHolder {

        public FootLoadStateViewHolder(View itemView) {
            super(itemView);
        }
    }
}
