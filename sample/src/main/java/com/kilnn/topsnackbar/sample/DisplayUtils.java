package com.kilnn.topsnackbar.sample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Kilnn on 2017/4/6.
 */

public class DisplayUtils {

    /**
     * 获取屏幕尺寸，不包括虚拟键(导航条)部分。
     *
     * @param context context参数
     * @return 屏幕尺寸
     */
    public static Point getScreenSize(Context context) {
        Point point = new Point();
        point.x = context.getResources().getDisplayMetrics().widthPixels;
        point.y = context.getResources().getDisplayMetrics().heightPixels;
        return point;
    }

    /**
     * 获取屏幕真实尺寸。
     * 参考：http://blog.csdn.net/gh8609123/article/details/53065231
     *
     * @param context context参数
     * @return 真实尺寸。
     */
    public static Point getRawScreenSize(Context context) {
        Point size = new Point();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else {
            try {
                Method getRawWidthMethod = Display.class.getMethod("getRawWidth");
                Method getRawHeightMethod = Display.class.getMethod("getRawHeight");
                size.x = (int) getRawWidthMethod.invoke(display);
                size.y = (int) getRawHeightMethod.invoke(display);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (size.x == 0 || size.y == 0) {
            return getScreenSize(context);
        } else {
            return size;
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context参数
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    /**
     * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定设备是否有导航条功能。
     * 注意：设备有导航条功能，并不意味着导航条一定显示，部分手机的导航条可以动态的显示和隐藏。
     * <p>
     * 这个方法判断很不准确，尽量少使用
     * {@link #isNavigationBarShow(Context)}
     *
     * @param context context参数
     * @return 是否有导航条功能
     */
    @Deprecated
    public static boolean hasNavigationBar(Context context) {
        boolean hasMenuKey = false;
        boolean hasBackKey = false;
        try {
            hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
        try {
            hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
        return !hasMenuKey && !hasBackKey;
    }

    /**
     * 通过设备屏幕的尺寸的差异，来判断导航条是否正在显示。
     *
     * @param context context参数
     * @return 导航条是否正在显示
     */
    public static boolean isNavigationBarShow(Context context) {
        Point point1 = getScreenSize(context);
        Point point2 = getRawScreenSize(context);
        boolean portrait = point1.x < point1.y;//是否是竖屏
        if (portrait) {
            return point1.y < point2.y;
        } else {
            return point1.x < point2.x;
        }
    }

    /**
     * 获取导航栏的高度。
     * 注意：该方法并不检测导航栏是否可用或者是否在显示。
     * {@link #getNavigationBarHeightIfShow(Context)}
     *
     * @param context context参数
     * @return 导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    /**
     * 如果导航条正在显示，那么获取导航栏的高度，否则返回0.
     *
     * @param context context参数
     * @return 导航栏高度
     */
    public static int getNavigationBarHeightIfShow(Context context) {
        if (isNavigationBarShow(context)) {
            return getNavigationBarHeight(context);
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
