package com.ds.sapling.hookdemo;

import java.util.List;

public interface PermissionListener {
    void grantedPermission();
    void cancelPermission(List permissions);
    void deniedPermission(List permissions);
}
