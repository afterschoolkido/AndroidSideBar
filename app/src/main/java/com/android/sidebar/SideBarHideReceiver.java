package com.android.sidebar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.sidebar.views.SideBarArrow;

/**
 * a receiver to accept broadcast form launcher to hide the sidebar.
 *
 * @author majh
 */
public class SideBarHideReceiver extends BroadcastReceiver {

    private SideBarArrow mLeft = null;
    private SideBarArrow mRight = null;

    private static final String ACTION_HIDE = "com.android.sidebar.ACTION_HIDE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_HIDE)) {
            if (null != mLeft || null != mRight) {
                mLeft.launcherInvisibleSideBar();
                mRight.launcherInvisibleSideBar();
            }
        }
    }

    public void setSideBar(SideBarArrow left, SideBarArrow right) {
        this.mLeft = left;
        this.mRight = right;
    }

}
