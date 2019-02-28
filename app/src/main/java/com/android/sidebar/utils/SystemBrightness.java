package com.android.sidebar.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

/**
 * A util to get and set sys brightness
 *
 * @author majh
 */
public class SystemBrightness {

    public static int getBrightness(Context context) {
        int brightValue = 0;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightValue;
    }

    public static void setBrightness(Context context, int brightValue) {
        try {
            // change brightness mode
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightValue);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
