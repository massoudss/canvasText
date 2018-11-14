package ir.logcat.canvastext.utils;

import android.content.Context;
import android.util.TypedValue;

public class Utils {


    public static int dpToPx(Context context, int dp) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static int spToPx(Context context, int sp) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics()));
    }

}
