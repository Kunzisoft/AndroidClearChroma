package com.kunzisoft.androidclearchroma.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;

public class Utility {

    public static void updatetatusBar(Activity activity, int newColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(darkenColor(newColor));
        }
    }

    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

}
