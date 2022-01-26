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

/**
 * LayoutManager-Horizontal 分割线
 */
public class ItemHorizontalDivider extends RecyclerView.ItemDecoration {

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
                drawLeft(c, child);
            }

        } else {

            int start = 0;

            if (itemPosition == 0)
                // 如果当前绘制的是第一个item，则不画分割线
                start = 1;

            for (int i = start; i < childCount; i++) {
                View child = parent.getChildAt(i);
                drawLeft(c, child);
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
    private void drawLeft(Canvas c, View mChild) {
        RecyclerView.LayoutParams mChildLayoutParams = (RecyclerView.LayoutParams) mChild.getLayoutParams();
        int left = mChild.getLeft() - mDividerHeight - mChildLayoutParams.leftMargin;
        int top = mChild.getTop() - mChildLayoutParams.topMargin + mMarginTop;
        int right = mChild.getLeft() - mChildLayoutParams.leftMargin;
        int bottom = mChild.getBottom() + mChildLayoutParams.bottomMargin - mMarginBottom;
        c.drawRect(left, top, right, bottom, mPaint);
    }

}