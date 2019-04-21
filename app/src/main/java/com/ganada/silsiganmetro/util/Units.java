package com.ganada.silsiganmetro.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Units {

    public static int dp(int dp){
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int m = metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;

        return dp * m;
    }

    public static float dp(float dp) {
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int m = metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;

        return dp * m;
    }
}
