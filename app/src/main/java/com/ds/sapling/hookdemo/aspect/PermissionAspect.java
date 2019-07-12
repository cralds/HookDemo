package com.ds.sapling.hookdemo.aspect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ds.sapling.hookdemo.PermissionActivity;
import com.ds.sapling.hookdemo.PermissionListener;
import com.ds.sapling.hookdemo.PermissionUtils;
import com.ds.sapling.hookdemo.permission.annotation.PermissionCancel;
import com.ds.sapling.hookdemo.permission.annotation.PermissionDenied;
import com.ds.sapling.hookdemo.permission.annotation.RequestPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 通过aspect实现6.0权限管理
 */
@Aspect
public class PermissionAspect {


    @Pointcut("execution(@com.ds.sapling.hookdemo.permission.annotation.RequestPermission * *(..)) && @annotation(requestPermission)")
    public void pointCutPermission(RequestPermission requestPermission){}

    @Around("pointCutPermission(requestPermission)")
    public void dealPermission(final ProceedingJoinPoint joinPoint, final RequestPermission requestPermission){
        Context context = null;
        final Object o = joinPoint.getThis();
        if (o instanceof Activity){
            context = (Context) o;
        }else if (o instanceof Fragment){
            context = ((Fragment) o).getActivity();
        }
        if (context == null){
            return;
        }

        if (PermissionUtils.hasSelfPermissions(context,requestPermission.value())){
            try {
                joinPoint.proceed();
                return;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        Intent intent = new Intent(context,PermissionActivity.class);
        intent.putExtra(PermissionActivity.REQUEST_PERMISSION,requestPermission.value());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        PermissionActivity.setPermissionListener(new PermissionListener() {
            @Override
            public void grantedPermission() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancelPermission(List permissions) {
                Class cls = o.getClass();
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods){
                    boolean isPermissionCancel =  method.isAnnotationPresent(PermissionCancel.class);
                    if (isPermissionCancel){
                        PermissionCancel permissionCancel = method.getAnnotation(PermissionCancel.class);
                        if (permissionCancel.requestCode() == requestPermission.requestCode()){
                            try {
                                method.invoke(o,permissions);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void deniedPermission(List permissions) {
                Class cls = o.getClass();
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods){
                    boolean hadDeniedMethod = method.isAnnotationPresent(PermissionDenied.class);
                    if (hadDeniedMethod){
                        PermissionDenied permissionDenied = method.getAnnotation(PermissionDenied.class);
                        if (permissionDenied.requestCode() == requestPermission.requestCode()){
                            try {
                                method.invoke(o,permissions);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

    }


    public void log(String msg){
        Log.d("PermissionAspect",msg);
    }
}
