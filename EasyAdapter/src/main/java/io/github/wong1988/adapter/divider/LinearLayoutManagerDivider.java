package io.github.wong1988.adapter.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.github.wong1988.adapter.BaseListAdapter;

public class LinearLayoutManagerDivider {

    /**
     * 分割线颜色#cccccc，宽度1px
     */
    public static RecyclerView.ItemDecoration getVerticalDivider() {
        return new ItemVerticalDivider();
    }

    /**
     * @param color     the divider color.
     * @param divHeight the divider height.
     */
    public static RecyclerView.ItemDecoration getVerticalDivider(@ColorInt int color, int divHeight) {
        return new ItemVerticalDivider(color, divHeight);
    }

    /**
     * @param color       the divider color.
     * @param divHeight   the divider height.
     * @param marginStart marginStart >=0，为线的左间距，默认0
     * @param marginEnd   marginEnd >=0，为线的右间距，默认0
     */
    public static RecyclerView.ItemDecoration getVerticalDivider(@ColorInt int color, int divHeight, int marginStart, int marginEnd) {
        return new ItemVerticalDivider(color, divHeight, marginStart, marginEnd);
    }

    /**
     * 分割线颜色#cccccc，宽度1px
     */
    public static RecyclerView.ItemDecoration getHorizontalDivider() {
        return new ItemHorizontalDivider();
    }

    /**
     * @param color     the divider color.
     * @param divHeight the divider height.
     */
    public static RecyclerView.ItemDecoration getHorizontalDivider(@ColorInt int color, int divHeight) {
        return new ItemHorizontalDivider(color, divHeight);
    }

    /**
     * @param color        the divider color.
     * @param divHeight    the divider height.
     * @param marginTop    marginTop >=0，为线的上间距，默认0
     * @param marginBottom marginBottom >=0，为线的下间距，默认0
     */
    public static RecyclerView.ItemDecoration getHorizontalDivider(@ColorInt int color, int divHeight, int marginTop, int marginBottom) {
        return new ItemHorizontalDivider(color, divHeight, marginTop, marginBottom);
    }

    /**
     * LayoutManager-Vertical 分割线
     */
    private static class ItemVerticalDivider extends RecyclerView.ItemDecoration {

        // 线高度
        private final int mDividerHeight;
        private final Paint mPaint;

        // 线的间距
        private final int mMarginStart;
        private final int mMarginEnd;

        /**
         * 构造方法，分割线颜色#cccccc，宽度1px
         */
        public ItemVerticalDivider() {
            this(Color.parseColor("#cccccc"), 1);
        }

        /**
         * 构造方法
         *
         * @param color     the divider color.
         * @param divHeight the divider height.
         */
        public ItemVerticalDivider(@ColorInt int color, int divHeight) {
            this(color, divHeight, 0, 0);
        }

        /**
         * 构造方法
         *
         * @param color       the divider color.
         * @param divHeight   the divider height.
         * @param marginStart marginStart >=0，为线的左间距，默认0
         * @param marginEnd   marginEnd >=0，为线的右间距，默认0
         */
        public ItemVerticalDivider(@ColorInt int color, int divHeight, int marginStart, int marginEnd) {
            mDividerHeight = divHeight;
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            if (marginStart < 0)
                marginStart = 0;
            if (marginEnd < 0)
                marginEnd = 0;
            this.mMarginStart = marginStart;
            this.mMarginEnd = marginEnd;
        }


        @Override
        public final void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 总共绘制数量
            int childCount = parent.getChildCount();

            // 获取第一个绘制的真实position
            int itemPosition = -1;

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

                if (itemPosition < headerLayoutCount + 1)
                    start = headerLayoutCount + 1 - itemPosition;

                int end = childCount;

                if (itemPosition + childCount > attachDataSize + headerLayoutCount)
                    end = childCount - ((itemPosition + childCount) - (attachDataSize + headerLayoutCount));

                for (int i = start; i < end; i++) {
                    View child = parent.getChildAt(i);
                    // 绘制顶部的分割线
                    drawTop(c, child);
                }

            } else {

                int start = 0;

                if (itemPosition == 0)
                    // 如果当前绘制的是第一个item，则不画分割线
                    start = 1;

                for (int i = start; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawTop(c, child);
                }
            }
        }

        @Override
        public final void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 获取当前的position
            int itemPosition = parent.getChildAdapterPosition(view);

            if (adapter instanceof BaseListAdapter) {
                // 头布局
                int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
                // 真实数据源长度
                int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();
                // 每个Item上面加一个线高（实际数据源第二个Item开始，且到真实数据源的最后一个Item）
                if (itemPosition > headerLayoutCount && itemPosition < attachDataSize + headerLayoutCount)
                    outRect.top = mDividerHeight;

            } else {
                if (itemPosition != 0)
                    outRect.top = mDividerHeight;
            }

        }

        /**
         * 绘制顶部分割线
         *
         * @param c      绘制容器
         * @param mChild 对应ItemView
         */
        private void drawTop(Canvas c, View mChild) {
            RecyclerView.LayoutParams mChildLayoutParams = (RecyclerView.LayoutParams) mChild.getLayoutParams();
            int left = mChild.getLeft() - mChildLayoutParams.leftMargin + mMarginStart;
            int top = mChild.getTop() - mChildLayoutParams.topMargin - mDividerHeight;
            int right = mChild.getRight() + mChildLayoutParams.rightMargin - mMarginEnd;
            int bottom = mChild.getTop() - mChildLayoutParams.topMargin;

            if (left > right)
                left = right;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * LayoutManager-Horizontal 分割线
     */
    private static class ItemHorizontalDivider extends RecyclerView.ItemDecoration {

        // 线高度
        private final int mDividerHeight;
        private final Paint mPaint;

        // 线的间距
        private final int mMarginTop;
        private final int mMarginBottom;

        /**
         * 构造方法，分割线颜色浅灰，宽度1px
         */
        public ItemHorizontalDivider() {
            this(Color.parseColor("#F2F2F2"), 1);
        }

        /**
         * 构造方法
         *
         * @param color     the divider color.
         * @param divHeight the divider height.
         */
        public ItemHorizontalDivider(@ColorInt int color, int divHeight) {
            this(color, divHeight, 0, 0);
        }

        /**
         * 构造方法
         *
         * @param color        the divider color.
         * @param divHeight    the divider height.
         * @param marginTop    marginTop >=0，为线的上间距，默认0
         * @param marginBottom marginBottom >=0，为线的下间距，默认0
         */
        public ItemHorizontalDivider(@ColorInt int color, int divHeight, int marginTop, int marginBottom) {
            mDividerHeight = divHeight;
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            if (marginTop < 0)
                marginTop = 0;
            if (marginBottom < 0)
                marginBottom = 0;
            this.mMarginTop = marginTop;
            this.mMarginBottom = marginBottom;
        }


        @Override
        public final void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 总共绘制数量
            int childCount = parent.getChildCount();

            // 获取第一个绘制的真实position
            int itemPosition = -1;

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

                if (itemPosition < headerLayoutCount + 1)
                    start = headerLayoutCount + 1 - itemPosition;

                int end = childCount;

                if (itemPosition + childCount > attachDataSize + headerLayoutCount)
                    end = childCount - ((itemPosition + childCount) - (attachDataSize + headerLayoutCount));

                for (int i = start; i < end; i++) {
                    View child = parent.getChildAt(i);
                    // 绘制左侧的分割线
                    drawLeft(c, child, parent);
                }

            } else {

                int start = 0;

                if (itemPosition == 0)
                    // 如果当前绘制的是第一个item，则不画分割线
                    start = 1;

                for (int i = start; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawLeft(c, child, parent);
                }
            }
        }

        @Override
        public final void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.Adapter adapter = parent.getAdapter();

            // 获取当前的position
            int itemPosition = parent.getChildAdapterPosition(view);

            if (adapter instanceof BaseListAdapter) {
                // 头布局
                int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
                // 真实数据源长度
                int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();
                // 每个Item左边加一个线高（实际数据源第二个Item开始，且到真实数据源的最后一个Item）
                if (itemPosition > headerLayoutCount && itemPosition < attachDataSize + headerLayoutCount)
                    outRect.left = mDividerHeight;

            } else {
                if (itemPosition != 0)
                    outRect.left = mDividerHeight;
            }

        }


        /**
         * 绘制左边分割线
         *
         * @param c      绘制容器
         * @param mChild 对应ItemView
         */
        private void drawLeft(Canvas c, View mChild, RecyclerView recyclerView) {
            int maxBottom = recyclerView.getHeight() - recyclerView.getPaddingBottom() - mMarginBottom;
            RecyclerView.LayoutParams mChildLayoutParams = (RecyclerView.LayoutParams) mChild.getLayoutParams();
            int left = mChild.getLeft() - mDividerHeight - mChildLayoutParams.leftMargin;
            int top = mChild.getTop() - mChildLayoutParams.topMargin + mMarginTop;
            int right = mChild.getLeft() - mChildLayoutParams.leftMargin;
            int bottom = mChild.getBottom() + mChildLayoutParams.bottomMargin - mMarginBottom;
            if (maxBottom > 0 && bottom > maxBottom)
                // 防止item与recyclerview设置高度一致且recyclerview设置padding时
                // 导致线过长的问题
                bottom = maxBottom;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
