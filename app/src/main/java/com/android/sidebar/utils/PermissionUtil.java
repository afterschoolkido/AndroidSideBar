package com.android.sidebar.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * -|-
 * @author majh
 */
public class PermissionUtil {

    /**
     * @param context
     * @return
     * AccessibilityService permission check
     */
    public static boolean isAccessibilityServiceEnable(Context context) {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        assert accessibilityManager != null;
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().contains(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isSettingsCanWrite(Context context) {
        return Settings.System.canWrite(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isCanDrawOverlays(Context context) {
        return Settings.canDrawOverlays(context);
    }
}
