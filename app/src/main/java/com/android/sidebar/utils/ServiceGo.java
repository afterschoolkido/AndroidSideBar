package com.android.sidebar.utils;

import android.content.Context;
import android.content.Intent;

import com.android.sidebar.SideBarService;

/**
 * launch the sidebar service
 * @author majh
 */
public class ServiceGo {

    public static void launchAccessibility(Context context) {
        Intent intent = new Intent(context, SideBarService.class);
        context.startService(intent);
    }
}
