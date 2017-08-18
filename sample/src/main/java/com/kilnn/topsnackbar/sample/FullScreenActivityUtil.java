package com.kilnn.topsnackbar.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Kilnn on 2017/4/5.
 * 设置Activity全屏的帮助类，即设置状态栏和导航栏为沉浸式。
 * <p>
 * 用法1：StatusBar背景和Toolbar背景不一致
 * {@link #setFullScreen(Activity, boolean, boolean, int, int)}  指定StatusBar颜色
 * {@link #setFitStatusBar(View)} 传入RootView，对RootView设置padding
 * <p>
 * 用法2：只显示Toolbar背景
 * {@link #setFullScreen(Activity, boolean, boolean)}  指定StatusBar颜色为透明
 * {@link #setFitStatusBar(View)} 传入Toolbar，对Toolbar设置padding值
 */
public class FullScreenActivityUtil {

    public static void setFullScreen(Activity activity, boolean tintStatusBar, boolean tintNavigationBar) {
        setFullScreen(activity, tintStatusBar, tintNavigationBar, Color.TRANSPARENT, Color.TRANSPARENT);
    }

    /**
     * 设置Activity全屏
     *
     * @param activity
     * @param tintStatusBar
     * @param tintNavigationBar
     */
    public static void setFullScreen(Activity activity, boolean tintStatusBar, boolean tintNavigationBar, int statusBarColor, int navigationBarColor) {
        if (!tintStatusBar && !tintNavigationBar) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentKitKat(activity, tintStatusBar, tintNavigationBar, true);
        } else {
            /**
             * 适配5.1以上 {@see http://blog.csdn.net/fengye810130/article/details/44461059}
             */
            setTranslucentKitKat(activity, tintStatusBar, tintNavigationBar, false);
            setTranslucentLollipop(activity, tintStatusBar, tintNavigationBar, statusBarColor, navigationBarColor);
        }
    }

    /**
     * 4.4到5.1版本的设置
     *
     * @param on
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentKitKat(Activity activity, boolean tintStatusBar, boolean tintNavigationBar, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = 0;
        if (tintStatusBar) {
            bits |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
        if (tintNavigationBar) {
            bits |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        }
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    /**
     * 5.1及以上的版本设置
     *
     * @param activity
     * @param tintStatusBar
     * @param tintNavigationBar
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentLollipop(Activity activity, boolean tintStatusBar, boolean tintNavigationBar, int statusBarColor, int navigationBarColor) {
        Window window = activity.getWindow();
        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (tintStatusBar) {
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.setStatusBarColor(statusBarColor);
        }
        if (tintNavigationBar) {
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.setNavigationBarColor(navigationBarColor);
        }
        window.getDecorView().setSystemUiVisibility(visibility);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }


    /**
     * {@link ViewCompat#setFitsSystemWindows(View, boolean)}方法会对StatusBar的fit处理是
     * 对View添加一个paddingTop值，但是并不会增加视图的高度，这对不是MATCH_PARENT或者WRAP_CONTENT的视图可能
     * 会导致视图显示范围的变小。此方法会设置一个paddingTop值，同时高度也会对应的增加。
     * 需要注意的是，View本身不能有paddingTop值，即不能再xml或者代码中额外设置paddingTop值。
     */
    public static void setFitStatusBar(final View view) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt < Build.VERSION_CODES.KITKAT) {
            ViewCompat.setFitsSystemWindows(view, true);
        } else if (sdkInt < Build.VERSION_CODES.LOLLIPOP) {
            int padding = DisplayUtils.getStatusBarHeight(view.getContext());
            setViewPadding(view, padding);
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    int padding = insets.getSystemWindowInsetTop();
                    setViewPadding(view, padding);
                    return insets.consumeSystemWindowInsets();
                }
            });
        }
    }

    private static void setViewPadding(View view, int padding) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是 MATCH_PARENT 或者 WRAP_CONTENT,那么不用累加，设置padding时会自动扩展。
        if (params.height >= 0) {
            //先减去上一次可能增加的PaddingTop
            params.height = Math.max(0, params.height - view.getPaddingTop());
            params.height += padding;
        }
        view.setPadding(
                view.getPaddingLeft(),
                padding,
                view.getPaddingRight(),
                view.getPaddingBottom()
        );
    }

    /**
     * {@link ViewCompat#setFitsSystemWindows(View, boolean)}方法会对StatusBar的fit处理是
     * 对View添加一个paddingTop值，但是并不会增加视图的高度，这对不是MATCH_PARENT或者WRAP_CONTENT的视图可能
     * 会导致视图显示范围的变小。此方法会设置一个paddingTop值，同时高度也会对应的增加。
     * 需要注意的是，View本身不能有paddingTop值，即不能再xml或者代码中额外设置paddingTop值。
     */
    public static void setFitStatusBarMargin(final View view) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt < Build.VERSION_CODES.KITKAT) {
            ViewCompat.setFitsSystemWindows(view, true);
        } else if (sdkInt < Build.VERSION_CODES.LOLLIPOP) {
            int padding = DisplayUtils.getStatusBarHeight(view.getContext());
            setViewMargin(view, padding);
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    int padding = insets.getSystemWindowInsetTop();
                    setViewMargin(view, padding);
                    return insets.consumeSystemWindowInsets();
                }
            });
        }
    }

    private static void setViewMargin(View view, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin += margin;
    }


}
