package io.github.wong1988.adapter.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import io.github.wong1988.adapter.BaseListAdapter;

/**
 * 由于流式布局item高度不一致，最后一行，
 * 画线会导致不是多一块（以相邻高的item画线）就是少一块（以相邻矮的item画线）的视觉
 * 所以不再支持分割线的颜色
 */
public class StaggeredGridLayoutManagerDivider {

    /**
     * 分割线颜色透明，宽度1px
     */
    public static RecyclerView.ItemDecoration getVerticalDivider() {
        return new ItemStaggeredGridVerticalDivider();
    }

    /**
     * @param divHeight the divider height.
     */
    public static RecyclerView.ItemDecoration getVerticalDivider(int divHeight) {
        return new ItemStaggeredGridVerticalDivider(divHeight);
    }

    /**
     * 分割线颜色默认透明，宽度1px
     */
//    public static RecyclerView.ItemDecoration getHorizontalDivider() {
//        return new ItemGridHorizontalDivider();
//    }

    /**
     * @param color     the divider color.
     * @param divHeight the divider height.
     */
//    public static RecyclerView.ItemDecoration getHorizontalDivider(@ColorInt int color, int divHeight) {
//        return new ItemGridHorizontalDivider(color, divHeight);
//    }

    /**
     * GridManager-Vertical 分割线
     */
    private static class ItemStaggeredGridVerticalDivider extends RecyclerView.ItemDecoration {

        // 线高度
        private final int mDividerHeight;

        // 第一个是否是全合并
        private boolean mFirstIsFull;

        /**
         * 构造方法，分割线颜色默认透明，宽度1px
         */
        public ItemStaggeredGridVerticalDivider() {
            this(1);
        }

        /**
         * 构造方法
         *
         * @param divHeight the divider height.
         */
        public ItemStaggeredGridVerticalDivider(int divHeight) {
            mDividerHeight = divHeight;
        }

        @Override
        public final void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 获取当前的position
            int itemPosition = parent.getChildAdapterPosition(view);

            // 获取布局管理器
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            // 获取设置的每行的item个数
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            // 当前position的spanSize,瀑布流与Grid区别是 只能所有合并 或者不合并
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanSize = params.isFullSpan() ? spanCount : 1;
            // 当前position在行中的位置 == 0 表示此行的最左侧
            // eg. 当前每行3个，position = 0 占2个，position = 1 占1个，position... 占1个
            // 则position = 0 的 spanIndex 为 0，position = 1 的 spanIndex 为 2，
            // position = 2 的 spanIndex 为 0，position = 3 的 spanIndex 为 1，position = 4 的 spanIndex 为 2
            int spanIndex = params.getSpanIndex();

            if (adapter instanceof BaseListAdapter) {

                // 头布局
                int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
                // 真实数据源长度
                int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();

                // 除去头布局的第一个判断是否是全合并
                if (itemPosition - headerLayoutCount == 0) {
                    mFirstIsFull = spanSize == spanCount;
                }

                // 是否达到数据源的第二行
                boolean rowDataTwo = false;
                if (headerLayoutCount == 1) {
                    // 有头布局
                    if (itemPosition > 0) {
                        // 数据源开始的位置
                        if (mFirstIsFull) {
                            if (itemPosition != 1)
                                rowDataTwo = true;
                        } else {
                            if (itemPosition - 1 > spanIndex)
                                rowDataTwo = true;
                        }
                    }
                } else {
                    // 无头布局
                    if (mFirstIsFull) {
                        if (itemPosition != 0)
                            rowDataTwo = true;
                    } else {
                        if (itemPosition > spanIndex)
                            rowDataTwo = true;
                    }
                }


                // 画顶部 （实际数据源第2行Item开始，且到真实数据源的最后一个Item）
                if (rowDataTwo && itemPosition < attachDataSize + headerLayoutCount)
                    outRect.top = mDividerHeight;

                if (itemPosition >= headerLayoutCount) {
                    if (spanSize != spanCount) {
                        // 绘制左右偏移
                        // 其实这里就是个找规律
                        /**
                         * 示例：线宽20 一行4个
                         * spanSize都为1的时候
                         * 第一行：0、15,5、10,10、5,15,0（四个item的左右偏移量）
                         * 简单来说最左侧的item的左边的偏移量0，最右侧的item的右边的偏移量为0（整行来说）
                         * 然后相邻的item，左右侧偏移量相加为线宽 15+5、10+10、5+15
                         * 如果spanSize 为 1、2、1的情况
                         * 第二行：0、15,5、5,15、0
                         * 同样一行的最左侧偏移量0，最右侧偏移量0，相邻item偏移量相加为线宽 15+5、5+15
                         * 可以得到一个规律：线宽/设置的一行几个 = 5（当前设定的变量下）
                         * 左侧的偏移量可以用当前item在本行的position得到左侧的偏移量
                         * 第一行：0*5、1*5、2*5、3*5 第二行：0*5、1*5、3(此值会自动加入合并的item的数量)*5
                         * 右侧的偏移量可以用 行中的index以及当前item占用的数量来得到右侧的偏移量
                         * 第一行：20 - (0+1)*5、20 - (1+1)*5、20 - (2+1)*5、20 - (3+1)*5
                         * 第二行：20 - (0+1)*5、20 - (1+2)*5、20 - (3+1)*5
                         */
                        outRect.left = spanIndex * mDividerHeight / spanCount;
                        outRect.right = mDividerHeight - (spanIndex + spanSize) * mDividerHeight / spanCount;
                    }
                }

            } else {

                // 说明不是全合并的，左右都要有偏移量
                if (spanSize != spanCount) {
                    // 绘制左右偏移
                    outRect.left = spanIndex * mDividerHeight / spanCount;
                    outRect.right = mDividerHeight - (spanIndex + spanSize) * mDividerHeight / spanCount;
                }

                if (itemPosition == 0) {
                    mFirstIsFull = spanSize == spanCount;
                }

                // 瀑布流只有 全合并 和 全不合并的情况
                // 首个全合并 只要itemPosition != 0 就画顶部
                // 首个不是全合并 只要 itemPosition > spanIndex 就画顶部
                if (mFirstIsFull) {
                    if (itemPosition != 0)
                        outRect.top = mDividerHeight;
                } else {
                    if (itemPosition > spanIndex)
                        outRect.top = mDividerHeight;
                }
            }

        }

    }

}
