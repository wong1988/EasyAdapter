package io.github.wong1988.adapter.holder;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RecyclerView.ViewHolder
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    private final SparseArray<View> mCacheViews;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mCacheViews = new SparseArray<>();
    }


    public final ImageView getImageView(int id) {
        return getView(id);
    }

    public final TextView getTextView(int id) {
        return getView(id);
    }

    public final EditText getEditText(int id) {
        return getView(id);
    }

    public final CompoundButton getCompoundButton(int id) {
        return getView(id);
    }

    public final Button getButton(int id) {
        return getView(id);
    }

    public final ImageButton getImageButton(int id) {
        return getView(id);
    }

    public final RadioButton getRadioButton(int id) {
        return getView(id);
    }

    public final CheckBox getCheckBox(int id) {
        return getView(id);
    }

    public final ProgressBar getProgressBar(int id) {
        return getView(id);
    }

    public final LinearLayout getLinearLayout(int id) {
        return getView(id);
    }

    public final RelativeLayout getRelativeLayout(int id) {
        return getView(id);
    }

    public final FrameLayout getFrameLayout(int id) {
        return getView(id);
    }

    public final Switch getSwitch(int id) {
        return getView(id);
    }

    public final ToggleButton getToggleButton(int id) {
        return getView(id);
    }

    public final <T extends View> T getView(int resId) {
        View view = mCacheViews.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            mCacheViews.put(resId, view);
        }
        return (T) view;
    }

    /***********常用方法************/

    public final void setVisibility(int viewId, @Visibility int visibility) {
        getView(viewId).setVisibility(visibility);
    }

    public final void setText(int viewId, CharSequence text) {
        getTextView(viewId).setText(text);
    }

    public final void setText(int viewId, @StringRes int resId) {
        getTextView(viewId).setText(resId);
    }

    public final void setTextColor(int viewId, int textColor) {
        getTextView(viewId).setTextColor(textColor);
    }

    public final void setImageDrawable(int viewId, @Nullable Drawable drawable) {
        getImageView(viewId).setImageDrawable(drawable);
    }

    public final void setChecked(int viewId, boolean checked) {
        getCompoundButton(viewId).setChecked(checked);
    }

    public final void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
    }
}
