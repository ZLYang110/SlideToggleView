package com.zlylib.slidetogglelib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;

/**
 * DisplayUtils
 *
 * @author onlyloveyd
 * @date 2019/1/10 09:02
 */
public class DisplayUtils {
    private static float mScreenDensity;

    /**
     * 获取手机屏幕像素密度
     *
     * @param context 上下文
     * @return 像素密度
     */
    public static float getDensity(Context context) {
        if (mScreenDensity <= 0) {
            mScreenDensity = context.getResources().getDisplayMetrics().density;
        }
        return mScreenDensity;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dp      相对像素密度值
     * @return 像素值
     */
    public static int dp2px(Context context, float dp) {
        return (int) (dp * getDensity(context) + 0.5f);
    }

}
