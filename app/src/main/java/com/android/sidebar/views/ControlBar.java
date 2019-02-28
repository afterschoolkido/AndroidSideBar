package com.android.sidebar.views;

import android.annotation.SuppressLint;
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
import android.widget.SeekBar;

import com.android.sidebar.R;
import com.android.sidebar.utils.DisplayUtil;
import com.android.sidebar.utils.SystemBrightness;
import com.android.sidebar.utils.SystemVolume;

/**
 * A bar to control brightness and volume
 *
 * @author majh
 */
class ControlBar {

    WindowManager.LayoutParams mParams;

    @SuppressLint("RtlHardcoded")
    LinearLayout getView(final Context context, boolean left, int tag, final SideBarContent sideBarContent) {
        mParams = new WindowManager.LayoutParams();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        LinearLayout seekBarLayout = (LinearLayout) inflater.inflate(R.layout.layout_seekbar, null);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        seekBarLayout.measure(w,h);
        // left or right seekbar
        if(left) {
            mParams.x = DisplayUtil.dp2px(context,120) - seekBarLayout.getMeasuredWidth();
            mParams.y = DisplayUtil.getScreenHeight(context) - DisplayUtil.dp2px(context,282);
            mParams.windowAnimations = R.style.LeftSeekBarAnim;
        }else {
            mParams.x = DisplayUtil.getScreenWidth(context) - DisplayUtil.dp2px(context,120);
            mParams.y = DisplayUtil.getScreenHeight(context) - DisplayUtil.dp2px(context,282);
            mParams.windowAnimations = R.style.RightSeekBarAnim;
        }
        VerticalSeekBar seekBar = seekBarLayout.findViewById(R.id.sb_);
        AppCompatImageView plus = seekBarLayout.findViewById(R.id.plus);
        AppCompatImageView less = seekBarLayout.findViewById(R.id.less);
        if(tag == 0) {
            // brightness control
            plus.setImageDrawable(context.getDrawable(R.drawable.ic_brightness_plus_));
            less.setImageDrawable(context.getDrawable(R.drawable.ic_brightness_less_));
            // brightness range 0~255
            seekBar.setMax(255);
            seekBar.setProgress(SystemBrightness.getBrightness(context));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SystemBrightness.setBrightness(context, progress);
                    sideBarContent.removeOrSendMsg(true,true);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }else if(tag == 1) {
            // volume control
            plus.setImageDrawable(context.getDrawable(R.drawable.ic_volume_plus_));
            less.setImageDrawable(context.getDrawable(R.drawable.ic_volume_less_));
            // volume range 0~15
            seekBar.setMax(15);
            seekBar.setProgress(SystemVolume.getVolume(context));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SystemVolume.setVolume(context, progress);
                    sideBarContent.removeOrSendMsg(true,true);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        return seekBarLayout;
    }
}
