package com.wujie.radarview;

import android.content.Context;

/**
 * Created by wujie on 2017/2/22.
 */
public class Utils {

    public static int dip2px (Context context, float dipValue) {
       float density = context.getResources().getDisplayMetrics().density;
        return (int) (density*dipValue + 0.5f);
    }

    public static  int px2dip(Context context, float pixValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return  (int) (pixValue / density + 0.5f);
    }
}
