package com.ds.sapling.hookdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.SimpleArrayMap;

public class PermissionUtils {

    /**
     * 判断是否所有权限都同意了，都同意返回true 否则返回false
     *
     * @param context     context
     * @param permissions permission list
     * @return return true if all permissions granted else false
     */
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断单个权限是否同意
     *
     * @param context    context
     * @param permission permission
     * @return return true if permission granted
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查是否都赋予权限
     *
     * @param grantResults grantResults
     * @return 所有都同意返回true 否则返回false
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) return false;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查所给权限List是否需要给提示
     *
     * @param activity    Activity
     * @param permissions 权限list
     * @return 如果某个权限需要提示则返回true
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }
}
