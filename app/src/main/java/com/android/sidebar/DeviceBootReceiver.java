package com.android.sidebar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.sidebar.utils.PermissionUtil;
import com.android.sidebar.utils.ServiceGo;

/**
 * device boot receiver
 *
 * @author majh
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(PermissionUtil.isCanDrawOverlays(context) && PermissionUtil.isAccessibilityServiceEnable(context)) {
                    ServiceGo.launchAccessibility(context);
                }else {
                    MainPageGo(context);
                }
            }else {
                if(PermissionUtil.isAccessibilityServiceEnable(context)) {
                    ServiceGo.launchAccessibility(context);
                }else {
                    MainPageGo(context);
                }
            }
        }
    }

    private void MainPageGo(Context context) {
        Intent launch = new Intent(context,MainActivity.class);
        launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }

}
