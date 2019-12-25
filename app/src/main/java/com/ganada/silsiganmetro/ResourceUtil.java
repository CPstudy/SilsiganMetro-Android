package com.ganada.silsiganmetro;

import android.content.Context;
import android.util.TypedValue;

public class ResourceUtil {
    public static int toPixel(Context context, int dpSize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, context.getResources().getDisplayMetrics());

    }
}
