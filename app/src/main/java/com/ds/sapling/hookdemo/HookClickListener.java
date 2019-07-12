package com.ds.sapling.hookdemo;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/**
 *  作者 cral
 *  创建日期 2019/7/8
 *  hook一个简单的点击事件
 **/
public class HookClickListener {
    private static final String TAG = HookClickListener.class.getSimpleName();
    public static void startHook(Context context, final View view){
        try {

            Class cls = Class.forName("android.view.View");
            Method method = cls.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);
            //获取到ListenerInfo对象
            Object listenerInfo = method.invoke(view);

            Class clsListenerInfo = Class.forName("android.view.View$ListenerInfo");
            Field field = clsListenerInfo.getDeclaredField("mOnClickListener");
            final View.OnClickListener clickListener = (View.OnClickListener) field.get(listenerInfo);

//            View.OnClickListener c = new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG,"hook到了onclickListener");
//                    try {
//
//                        Class clsI = Class.forName("android.view.View$OnClickListener");
//                        final Method mClick = clsI.getDeclaredMethod("onClick",View.class);
//                        mClick.setAccessible(true);
//                        mClick.invoke(clickListener,view);
//
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            };

            Object proxyListener = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.d(TAG,"hook到了onclickListener");
                    return method.invoke(clickListener,args);
                }
            });
            //把代理的listener赋值给ListenerInfo
            field.set(listenerInfo,proxyListener);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
