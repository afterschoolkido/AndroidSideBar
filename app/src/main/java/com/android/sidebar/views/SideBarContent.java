package com.android.sidebar.views;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.sidebar.R;
import com.android.sidebar.SideBarService;
import com.android.sidebar.utils.PermissionUtil;

/**
 * Sidebar left & right
 *
 * @author majh
 */
public class SideBarContent implements View.OnClickListener {

    private Context mContext;
    private boolean mLeft;
    private LinearLayout mContentView;
    private WindowManager mWindowManager;
    private LinearLayout mArrowView;
    private SideBarService mSideBarService;
    private ControlBar mControlBar;
    private LinearLayout mSeekBarView;
    private LinearLayout mAnotherArrowView;
    private int mTagTemp = -1;

    private static final int COUNT_DOWN_TAG = 1;
    private static final int COUNT_DWON_TIME = 5000;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT_DOWN_TAG:
                    goNormal();
                    break;
            }
        }
    };

    LinearLayout getView(Context context,
                         boolean left,
                         WindowManager windowManager,
                         WindowManager.LayoutParams params,
                         LinearLayout arrowView,
                         SideBarService sideBarService,
                         LinearLayout anotherArrowView) {
        mContext = context;
        mLeft = left;
        mWindowManager = windowManager;
        mArrowView = arrowView;
        mSideBarService = sideBarService;
        mAnotherArrowView = anotherArrowView;
        // get layout
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = (LinearLayout) inflater.inflate(R.layout.layout_content, null);
        // init click
        mContentView.findViewById(R.id.tv_brightness).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_back).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_home).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_annotation).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_volume).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_backstage).setOnClickListener(this);
        LinearLayout root = mContentView.findViewById(R.id.root);
        if(left) {
            root.setPadding(15,0,0,0);
        }else {
            root.setPadding(0,0,15,0);
        }
        mWindowManager.addView(mContentView,params);
        return mContentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_brightness:
                removeOrSendMsg(true,true);
                brightnessPermissionCheck();
                break;
            case R.id.tv_back:
                removeOrSendMsg(true,true);
                clearSeekBar();
                mSideBarService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                break;
            case R.id.tv_home:
                removeOrSendMsg(true,false);
                goNormal();
                mSideBarService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                break;
            case R.id.tv_annotation:
                removeOrSendMsg(true,false);
                goNormal();
                annotationGo();
                break;
            case R.id.tv_volume:
                removeOrSendMsg(true,true);
                brightnessOrVolume(1);
                break;
            case R.id.tv_backstage:
                removeOrSendMsg(true,false);
                goNormal();
                mSideBarService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                break;
        }
    }

    private void brightnessOrVolume(int tag) {
        if(mTagTemp == tag) {
            if(null != mSeekBarView) {
                removeSeekBarView();
            }else {
                addSeekBarView(tag);
            }
            return;
        }
        mTagTemp = tag;
        if(null == mControlBar) {
            mControlBar = new ControlBar();
        }
        if(null == mSeekBarView) {
            addSeekBarView(tag);
        }else {
            removeSeekBarView();
            addSeekBarView(tag);
        }
    }

    private void addSeekBarView(int tag) {
        mSeekBarView = mControlBar.getView(mContext,mLeft,tag,this);
        mWindowManager.addView(mSeekBarView, mControlBar.mParams);
    }

    private void removeSeekBarView() {
        if(null != mSeekBarView) {
            mWindowManager.removeView(mSeekBarView);
            mSeekBarView = null;
        }
    }

    private void arrowsShow() {
        mContentView.setVisibility(View.GONE);
        mArrowView.setVisibility(View.VISIBLE);
        mAnotherArrowView.setVisibility(View.VISIBLE);
    }

    void clearSeekBar() {
        if(null != mSeekBarView) {
            mWindowManager.removeView(mSeekBarView);
            mSeekBarView = null;
        }
    }

    private void goNormal() {
        arrowsShow();
        clearSeekBar();
    }

    private void annotationGo() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.notes", "com.android.notes.MainActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.app_not_find), Toast.LENGTH_SHORT).show();
        }
    }

    void removeOrSendMsg(boolean remove, boolean send) {
        if(remove) {
            mHandler.removeMessages(COUNT_DOWN_TAG);
        }
        if(send) {
            mHandler.sendEmptyMessageDelayed(COUNT_DOWN_TAG,COUNT_DWON_TIME);
        }
    }

    /**
     * when AccessibilityService is forced closed
     */
    void clearCallbacks() {
        if(null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void brightnessPermissionCheck() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtil.isSettingsCanWrite(mContext)) {
                goNormal();
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Toast.makeText(mContext,mContext.getString(R.string.setting_modify_toast),Toast.LENGTH_LONG).show();
            }else {
                brightnessOrVolume(0);
            }
        }else {
            brightnessOrVolume(0);
        }
    }
}
