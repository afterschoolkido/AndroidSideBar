package com.android.sidebar;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.sidebar.utils.PermissionUtil;

/**
 * home page
 *
 * @author majh
 */
public class MainActivity extends AppCompatActivity {

    private AppCompatButton mFlastWindowButton;
    private AppCompatButton mAccessibilityButton;
    private static final int FLAT_REQUEST_CODE = 213;
    private static final int ACCESSIBILITY_REQUEST_CODE = 438;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mFlastWindowButton = findViewById(R.id.btn_flatwindow);
        mAccessibilityButton = findViewById(R.id.btn_accessibility);
        flatWindowVisible();
    }

    /**
     * flut button visible
     */
    private void flatWindowVisible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // > M,grant permission
            if (PermissionUtil.isCanDrawOverlays(this)) {
                // permission authorized,service go,button gone
                mFlastWindowButton.setVisibility(View.GONE);
                accessibilityVisible();
            } else {
                // permission unauthorized,button visible
                mFlastWindowButton.setVisibility(View.VISIBLE);
                Toast.makeText(this,getString(R.string.permission_flatwindow_),Toast.LENGTH_SHORT).show();
            }
        } else {
            // < M,service go,gone
            mFlastWindowButton.setVisibility(View.GONE);
            accessibilityVisible();
        }
    }

    /**
     * Accessibility button visible
     */
    private void accessibilityVisible() {
        if(PermissionUtil.isAccessibilityServiceEnable(this)) {
            Toast.makeText(this,getString(R.string.permission_notice),Toast.LENGTH_SHORT).show();
            finish();
        }else {
            mAccessibilityButton.setVisibility(View.VISIBLE);
            Toast.makeText(this,getString(R.string.permission_accessibility_),Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goGetFlatWindow(View view) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivityForResult(intent,FLAT_REQUEST_CODE);
    }

    public void goGetAccessibility(View view) {
        Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(accessibleIntent,ACCESSIBILITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FLAT_REQUEST_CODE) {
            flatWindowVisible();
        }else if(requestCode == ACCESSIBILITY_REQUEST_CODE){
            accessibilityVisible();
        }
    }
}
