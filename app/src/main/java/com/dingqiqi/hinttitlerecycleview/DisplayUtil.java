package com.dingqiqi.hinttitlerecycleview;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/1.
 */
public class DisplayUtil {

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

}
