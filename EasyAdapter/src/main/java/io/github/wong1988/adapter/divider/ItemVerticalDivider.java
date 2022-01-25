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
 * LayoutManager-Vertical 分割线
 */
public class ItemVerticalDivider extends RecyclerView.ItemDecoration {

    // 线高度
    private final int mDividerHeight;
    private final Paint mPaint;

    // 线的间距
    private final int mMarginStart;
    private final int mMarginEnd;

    /**
     * 构造方法，分割线颜色浅灰，宽度1px
     */
    public ItemVerticalDivider() {
        this(Color.parseColor("#F2F2F2"), 1);
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

        if (adapter instanceof BaseListAdapter) {
            // 头布局
            int headerLayoutCount = ((BaseListAdapter) adapter).isCanvasHeader() ? 1 : 0;
            // 真实数据源长度
            int attachDataSize = ((BaseListAdapter) adapter).getAttachDataSize();

            for (int i = headerLayoutCount + 1; i < attachDataSize + headerLayoutCount; i++) {
                View child = parent.getChildAt(i);
                // 绘制顶部的分割线
                drawTop(c, child);
            }

        } else {
            int childCount = parent.getChildCount();
            for (int i = 1; i < childCount; i++) {
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
