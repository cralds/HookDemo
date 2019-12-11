package com.ds.sapling.hookdemo.aspect;

import android.util.Log;
import android.view.View;

import com.ds.sapling.hookdemo.permission.annotation.SingleClick;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * create by cral
 * create at 2019/12/11
 * 防重复点击
 **/
@Aspect
public class SingClickAspect {

    @Pointcut("execution(@com.ds.sapling.hookdemo.permission.annotation.SingleClick * *(..)) && @annotation(singleClick)")
    public void pointSingleClick(SingleClick singleClick){}

    @Around("pointSingleClick(singleClick)")
    public void dealSingleClick(ProceedingJoinPoint joinPoint,SingleClick singleClick){
        View view = null;
        for (Object o : joinPoint.getArgs()){
            if (o instanceof View){
                view = (View) o;
            }
        }
        if (view == null){
            return;
        }

        boolean isSingle = isSingleClick(view.getId(),singleClick.time());
        Log.d("ssssssssssssssssss","issingle====="+isSingle);
        if (isSingle){
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    private long clickTime = 0;
    private int viewId;

    private boolean isSingleClick(int id,long spaceTime){
        if (viewId != id){//点击控件不同，不是重复点击
            viewId = id;
            clickTime = System.currentTimeMillis();
            return true;
        }
        long time = System.currentTimeMillis() - clickTime;
        if (time < spaceTime){//重复点击
            viewId = id;
            clickTime = System.currentTimeMillis();
            return false;
        }
        viewId = id;
        clickTime = System.currentTimeMillis();
        return true;

    }
}
