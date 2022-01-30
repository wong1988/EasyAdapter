package io.github.wong1988.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.wong1988.adapter.BaseListAdapter;

public class GridLayoutManagerDivider {

    /**
     * 分割线颜色默认透明，宽度1px
     */
    public static RecyclerView.ItemDecoration getVerticalDivider() {
        return new ItemGridVerticalDivider();
    }

    /**
     * @param color     the divider color.
     * @param divHeight the divider height.
     */
    public static RecyclerView.ItemDecoration getVerticalDivider(@ColorInt int color, int divHeight) {
        return new ItemGridVerticalDivider(color, divHeight);
    }

    /**
     * GridManager-Vertical 分割线
     */
    private static class ItemGridVerticalDivider extends RecyclerView.ItemDecoration {

        // 线高度
        private final int mDividerHeight;
        private final Paint mPaint;

        /**
         * 构造方法，分割线颜色默认透明，宽度1px
         */
        public ItemGridVerticalDivider() {
            this(Color.parseColor("#00000000"), 1);
        }

        /**
         * 构造方法
         *
         * @param color     the divider color.
         * @param divHeight the divider height.
         */
        public ItemGridVerticalDivider(@ColorInt int color, int divHeight) {
            mDividerHeight = divHeight;
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public final void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 总共绘制数量
            int childCount = parent.getChildCount();

            // 获取第一个绘制的真实position
            int itemPosition;

            if (childCount > 0)
                itemPosition = parent.getChildAdapterPosition(parent.getChildAt(0));
            else
                return;

            if (adapter instanceof BaseListAdapter) {
                // 头布局
                int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
                // 真实数据源长度
                int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();

                int start = 0;

                if (itemPosition < headerLayoutCount)
                    start = headerLayoutCount - itemPosition;

                int end = childCount;

                if (itemPosition + childCount > attachDataSize + headerLayoutCount)
                    end = childCount - ((itemPosition + childCount) - (attachDataSize + headerLayoutCount));

                for (int i = start; i < end; i++) {
                    View child = parent.getChildAt(i);
                    // 绘制顶部的分割线
                    drawTop(c, child, parent);
                    drawRight(c, child, parent);
                }

            } else {
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawTop(c, child, parent);
                    drawRight(c, child, parent);
                }
            }
        }

        /**
         * 是否可以画顶部
         */
        private boolean isDrawTop(@NonNull RecyclerView parent, @NonNull View view) {
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (!(adapter instanceof BaseListAdapter))
                return true;

            // 获取布局管理器
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            // 获取当前的position
            int itemPosition = parent.getChildAdapterPosition(view);
            // 获取设置的每行的item个数
            int spanCount = gridLayoutManager.getSpanCount();
            // 当前position所在行的index
            int rowIndex = spanSizeLookup.getSpanGroupIndex(itemPosition, spanCount);

            int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
            int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();

            return rowIndex > headerLayoutCount && itemPosition < attachDataSize + headerLayoutCount;
        }

        /**
         * 是否可以画右侧的线，最右边的item是不允许画线的
         */
        private boolean isDrawRight(@NonNull RecyclerView parent, @NonNull View view) {
            // 获取布局管理器
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            // 获取当前的position
            int itemPosition = parent.getChildAdapterPosition(view);
            // 获取设置的每行的item个数
            int spanCount = gridLayoutManager.getSpanCount();
            // 当前position的spanSize
            int spanSize = spanSizeLookup.getSpanSize(itemPosition);
            int spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount);
            return spanSize + spanIndex < spanCount;
        }

        @Override
        public final void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 获取当前的position
            int itemPosition = parent.getChildAdapterPosition(view);

            // 获取布局管理器
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            // 获取设置的每行的item个数
            int spanCount = gridLayoutManager.getSpanCount();
            // 当前position的spanSize
            int spanSize = spanSizeLookup.getSpanSize(itemPosition);
            // 当前position在行中的位置 == 0 表示此行的最左侧
            // eg. 当前每行3个，position = 0 占2个，position = 1 占1个，position... 占1个
            // 则position = 0 的 spanIndex 为 0，position = 1 的 spanIndex 为 2，
            // position = 2 的 spanIndex 为 0，position = 3 的 spanIndex 为 1，position = 4 的 spanIndex 为 2
            int spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount);
            // 当前position所在行的index
            int rowIndex = spanSizeLookup.getSpanGroupIndex(itemPosition, spanCount);
            // 当前position所在列的index
            int columnIndex = spanIndex / spanSize;

            if (adapter instanceof BaseListAdapter) {
                // 头布局
                int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
                // 真实数据源长度
                int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();
                // 每个Item上面加一个线高（实际数据源第一个Item开始，且到真实数据源的最后一个Item）
                if (rowIndex > headerLayoutCount && itemPosition < attachDataSize + headerLayoutCount)
                    outRect.top = mDividerHeight;

                if (itemPosition >= headerLayoutCount) {
                    if (spanSize != spanCount) {
                        // 绘制左右分偏移
                        outRect.left = columnIndex * mDividerHeight / spanCount;
                        outRect.right = mDividerHeight - (columnIndex + 1) * mDividerHeight / spanCount;
                    }
                }

            } else {
                if (spanSize != spanCount) {
                    // 绘制左右便宜
                    outRect.left = columnIndex * mDividerHeight / spanCount;
                    outRect.right = mDividerHeight - (columnIndex + 1) * mDividerHeight / spanCount;
                }

                if (rowIndex != 0)
                    // 只要不是最顶部开始的item顶部就加线高
                    outRect.top = mDividerHeight;
            }
        }

        /**
         * 绘制顶部分割线，直接顶到最大值
         *
         * @param c      绘制容器
         * @param mChild 对应ItemView
         */
        private void drawTop(Canvas c, View mChild, RecyclerView recyclerView) {

            if (!isDrawTop(recyclerView, mChild))
                return;

            // 获取recyclerview的宽度
            int width = recyclerView.getWidth();
            int paddingRight = recyclerView.getPaddingRight();
            RecyclerView.LayoutParams mChildLayoutParams = (RecyclerView.LayoutParams) mChild.getLayoutParams();
            // marginLeft也要进行画线
            int left = mChild.getLeft() - mChildLayoutParams.leftMargin;
            int top = mChild.getTop() - mChildLayoutParams.topMargin - mDividerHeight;
            int right = mChild.getRight() + mChildLayoutParams.rightMargin;
            if (right < width - paddingRight) {
                // 防止如果最后一排的item未能全部填充会导致顶部分割线不全的情况
                right = width - paddingRight;
            }
            int bottom = mChild.getTop() - mChildLayoutParams.topMargin;

            if (left > right)
                left = right;

            c.drawRect(left, top, right, bottom, mPaint);
        }

        /**
         * 绘制右边分割线
         *
         * @param c            绘制容器
         * @param mChild       对应ItemView
         * @param recyclerView RecyclerView
         */
        private void drawRight(Canvas c, View mChild, RecyclerView recyclerView) {

            if (!isDrawRight(recyclerView, mChild))
                return;

            RecyclerView.LayoutParams mChildLayoutParams = (RecyclerView.LayoutParams) mChild.getLayoutParams();
            int left = mChild.getRight() + mChildLayoutParams.rightMargin;
            int right = left + mDividerHeight;
            int bottom = mChild.getBottom() + mChildLayoutParams.bottomMargin;
            int top = mChild.getTop() - mChildLayoutParams.topMargin;
            c.drawRect(left, top, right, bottom, mPaint);
        }

    }
}
