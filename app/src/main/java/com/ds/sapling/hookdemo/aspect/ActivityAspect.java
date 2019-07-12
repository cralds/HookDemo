package com.ds.sapling.hookdemo.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 采用aspectj来hook
 */
@Aspect
public class ActivityAspect {
    private static final String TAG = ActivityAspect.class.getSimpleName();
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void pointOnclick(){}

    @Pointcut("execution(* *..Activity.on**(..))")
    public void pointLifeCycle(){}

    @Pointcut("execution(* com.ds.sapling.hookdemo.presenter.*Presenter.**(..))")
    public void pointPresenter(){}

    @After("pointOnclick()")
    public void ajOnclick(JoinPoint point){
        String si = point.getSignature().toString();
        Log.d(TAG,"ajOnclick====="+si);
    }

    @After("pointLifeCycle()")
    public void ajLifeCycle(JoinPoint point){
        String si = point.getSignature().toString();
//        Log.d(TAG,"ajLifeCycle====="+si);
    }

    @Before("pointPresenter()")
    public void ajPresenter(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Log.d(TAG,"methodname====="+methodSignature.getMethod().getName());
    }
}
