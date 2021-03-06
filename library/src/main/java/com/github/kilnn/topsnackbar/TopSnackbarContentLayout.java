package com.github.kilnn.topsnackbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

/**
 * Created by Kilnn on 2017/8/13.
 */

public class TopSnackbarContentLayout extends LinearLayout implements
        BaseTransientTopBar.ContentViewCallback {

    private TextView mMessageView;
    private Button mActionView;

    private int mMaxWidth;
    private int mMaxInlineActionWidth;

    public TopSnackbarContentLayout(Context context) {
        this(context, null);
    }

    public TopSnackbarContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopSnackbarLayout);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.TopSnackbarLayout_android_maxWidth, -1);
        mMaxInlineActionWidth = a.getDimensionPixelSize(
                R.styleable.TopSnackbarLayout_maxActionInlineWidth, -1);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMessageView = (TextView) findViewById(R.id.snackbar_text);
        mActionView = (Button) findViewById(R.id.snackbar_action);
    }

    public TextView getMessageView() {
        return mMessageView;
    }

    public Button getActionView() {
        return mActionView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mMaxWidth > 0 && getMeasuredWidth() > mMaxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        final int multiLineVPadding = getResources().getDimensionPixelSize(
                R.dimen.design_snackbar_padding_vertical_2lines);
        final int singleLineVPadding = getResources().getDimensionPixelSize(
                R.dimen.design_snackbar_padding_vertical);
        final boolean isMultiLine = mMessageView.getLayout().getLineCount() > 1;

        boolean remeasure = false;
        if (isMultiLine && mMaxInlineActionWidth > 0
                && mActionView.getMeasuredWidth() > mMaxInlineActionWidth) {
            if (updateViewsWithinLayout(VERTICAL, multiLineVPadding,
                    multiLineVPadding - singleLineVPadding)) {
                remeasure = true;
            }
        } else {
            final int messagePadding = isMultiLine ? multiLineVPadding : singleLineVPadding;
            if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                remeasure = true;
            }
        }

        if (remeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private boolean updateViewsWithinLayout(final int orientation,
                                            final int messagePadTop, final int messagePadBottom) {
        boolean changed = false;
        if (orientation != getOrientation()) {
            setOrientation(orientation);
            changed = true;
        }
        if (mMessageView.getPaddingTop() != messagePadTop
                || mMessageView.getPaddingBottom() != messagePadBottom) {
            updateTopBottomPadding(mMessageView, messagePadTop, messagePadBottom);
            changed = true;
        }
        return changed;
    }

    private static void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view,
                    ViewCompat.getPaddingStart(view), topPadding,
                    ViewCompat.getPaddingEnd(view), bottomPadding);
        } else {
            view.setPadding(view.getPaddingLeft(), topPadding,
                    view.getPaddingRight(), bottomPadding);
        }
    }

    @Override
    public void animateContentIn(int delay, int duration) {
        ViewCompat.setAlpha(mMessageView, 0f);
        ViewCompat.animate(mMessageView).alpha(1f).setDuration(duration)
                .setStartDelay(delay).start();

        if (mActionView.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(mActionView, 0f);
            ViewCompat.animate(mActionView).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }
    }

    @Override
    public void animateContentOut(int delay, int duration) {
        ViewCompat.setAlpha(mMessageView, 1f);
        ViewCompat.animate(mMessageView).alpha(0f).setDuration(duration)
                .setStartDelay(delay).start();

        if (mActionView.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(mActionView, 1f);
            ViewCompat.animate(mActionView).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }
    }

}
