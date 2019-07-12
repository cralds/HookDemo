package com.ds.sapling.hookdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends Activity {

    public static final String REQUEST_PERMISSION = "permissions";
    public static PermissionListener permissionListener;

    public static void setPermissionListener(PermissionListener permissionListener) {
        PermissionActivity.permissionListener = permissionListener;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = getIntent().getStringArrayExtra(REQUEST_PERMISSION);
        if (permissions == null || permissions.length == 0){
            finish();
            return;
        }
        requestPermissions(permissions,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++)
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                deniedList.add(permissions[i]);

        if (deniedList.isEmpty()) {
            permissionListener.grantedPermission();
        }
        else if (!PermissionUtils.shouldShowRequestPermissionRationale(this,permissions)){
            //用户选择不再提示
            permissionListener.deniedPermission(deniedList);
        }else {
            permissionListener.cancelPermission(deniedList);
        }
        finish();

    }
}
