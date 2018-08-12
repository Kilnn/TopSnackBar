package com.github.kilnn.topsnackbar.sample.custom;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kilnn.topsnackbar.TopSnackbar;
import com.github.kilnn.topsnackbar.TopSnackbarContentLayout;
import com.github.kilnn.topsnackbar.sample.R;

/**
 * Created by Kilnn on 2017/8/13.
 */

public class TopSnackActionBar extends TopSnackbar {

    public TopSnackActionBar(ViewGroup parent, View content, ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    @NonNull
    public static TopSnackbar make(@NonNull View view, @NonNull CharSequence text,
                                   @Duration int duration) {
        final ViewGroup parent = findSuitableParent(view);
        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. "
                    + "Please provide a valid view.");
        }

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final TopSnackbarContentLayout content =
                (TopSnackbarContentLayout) inflater.inflate(R.layout.layout_top_snackbar_include, parent, false);
        final TopSnackbar snackbar = new TopSnackbar(parent, content, content);
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    @NonNull
    public static TopSnackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }


}
