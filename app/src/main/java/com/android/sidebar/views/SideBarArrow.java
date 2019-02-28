package com.android.sidebar.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.sidebar.R;
import com.android.sidebar.SideBarService;

/**
 * Arrow left & right
 *
 * @author majh
 */
public class SideBarArrow implements View.OnClickListener {

    private WindowManager.LayoutParams mParams;
    private LinearLayout mArrowView;
    private Context mContext;
    private boolean mLeft;
    private WindowManager mWindowManager;
    private SideBarService mSideBarService;
    private SideBarContent mContentBar;
    private LinearLayout mContentBarView;
    private LinearLayout mAnotherArrowView;

    public LinearLayout getView(Context context,boolean left,WindowManager windowManager,SideBarService sideBarService) {
        mContext = context;
        mLeft = left;
        mWindowManager = windowManager;
        mSideBarService = sideBarService;
        mParams = new WindowManager.LayoutParams();
        // compatible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        // set bg transparent
        mParams.format = PixelFormat.RGBA_8888;
        // can not focusable
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.x = 0;
        mParams.y = 0;
        // window size
        mParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // get layout
        LayoutInflater inflater = LayoutInflater.from(context);
        mArrowView = (LinearLayout) inflater.inflate(R.layout.layout_arrow, null);
        AppCompatImageView arrow = mArrowView.findViewById(R.id.arrow);
        arrow.setOnClickListener(this);
        if(left) {
            arrow.setRotation(180);
            mParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
            mParams.windowAnimations = R.style.LeftSeekBarAnim;
        }else {
            mParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
            mParams.windowAnimations = R.style.RightSeekBarAnim;
        }
        mWindowManager.addView(mArrowView,mParams);
        return mArrowView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow:
                mArrowView.setVisibility(View.GONE);
                mAnotherArrowView.setVisibility(View.GONE);
                if(null == mContentBar || null == mContentBarView) {
                    mContentBar = new SideBarContent();
                    mContentBarView = mContentBar.getView(mContext,mLeft,mWindowManager,mParams,mArrowView,mSideBarService, mAnotherArrowView);
                }else {
                    mContentBarView.setVisibility(View.VISIBLE);
                }
                mContentBar.removeOrSendMsg(false,true);
                break;
        }
    }

    public void setAnotherArrowBar(LinearLayout anotherArrowBar) {
        mAnotherArrowView = anotherArrowBar;
    }

    public void launcherInvisibleSideBar() {
        mArrowView.setVisibility(View.VISIBLE);
        if(null != mContentBar || null != mContentBarView) {
            mContentBarView.setVisibility(View.GONE);
            mContentBar.removeOrSendMsg(true,false);
            mContentBar.clearSeekBar();
        }
    }

    /**
     * when AccessibilityService is forced closed
     */
    public void clearAll() {
        mWindowManager.removeView(mArrowView);
        if(null != mContentBar || null != mContentBarView) {
            mWindowManager.removeView(mContentBarView);
            mContentBar.clearSeekBar();
            mContentBar.clearCallbacks();
        }
    }
}
