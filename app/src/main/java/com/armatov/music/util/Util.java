package com.armatov.music.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.armatov.music.R;
import com.armatov.music.service.MultiPlayer;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static int checkedItem = 0;

    public static int getActionBarSize(@NonNull Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public static Point getScreenSize(@NonNull Context c) {
        Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBarTranslucent(@NonNull Window window) {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void setAllowDrawUnderStatusBar(@NonNull Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void hideSoftKeyboard(@Nullable Activity activity) {
        if (activity != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    public static boolean isTablet(@NonNull final Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static boolean isLandscape(@NonNull final Resources resources) {
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int resolveDimensionPixelSize(@NonNull Context context, @AttrRes int dimenAttr) {
        TypedArray a = context.obtainStyledAttributes(new int[]{dimenAttr});
        int dimensionPixelSize = a.getDimensionPixelSize(0, 0);
        a.recycle();
        return dimensionPixelSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRTL(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration config = context.getResources().getConfiguration();
            return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        } else return false;
    }

    public static List<float[]> calculateFft(float[]data, float[]fft) {
        List<float[]> result = new ArrayList<>();
        float[] lastFFt = new float[1024 * 4];

        System.arraycopy(MultiPlayer.data, 0, data, 0, MultiPlayer.data.length / 2);
        System.arraycopy(MultiPlayer.lastFFt, 0, lastFFt, 0, MultiPlayer.lastFFt.length / 2);

        int lenth = lastFFt.length / 2;

        for (int i = 0; i < lenth - 5; i++) {
            float div = (lenth / 2) - i * 2;
            div = div > 20 ? (int) (div) : 20;
            float w = lastFFt[i * 2] * lastFFt[i * 2];
            float q = ((lastFFt[i * 2 + 1] * lastFFt[i * 2 + 1]));
            float res = (w + q) / div;
            res = res > 0 ? (int) (60 * Math.log10(res * res)) : 0;
            for (int f = 0; f <= 5; f++) {
                if (i <= 5) {
                    fft[f] = 1;
                }
            }
            for (int f = 0; f < 8; f++) {
                if ((i > 7) && (fft[i - f] > fft[(i - f) - 1])) {
                    fft[(i - f) - 1] = fft[(i - f)] - fft[(i - f)] / 5;
                }
            }

            fft[i + 5] = res;

        }
        for (int f = 0; f < 1000; f++) {
            for (int i = 0; i < 8; i++) {
                if (fft[f] > fft[f + 1]) {
                    fft[f + 1] = fft[f] - fft[(f)] / 5;
                }
            }
        }
        result.add(data);
        result.add(fft);
        return result;

    }

}
