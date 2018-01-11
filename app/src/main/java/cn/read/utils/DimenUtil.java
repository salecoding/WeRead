package cn.read.utils;


import cn.read.base.App;

/**
 * Created by lw on 2017/1/20.
 */

public class DimenUtil {
    public static float dp2px(float dp) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp) {
        final float scale = App.getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenSize() {
        return App.getContext().getResources().getDisplayMetrics().widthPixels;
    }
}
