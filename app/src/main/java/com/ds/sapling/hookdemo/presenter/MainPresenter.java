package com.ds.sapling.hookdemo.presenter;

import android.content.Context;
import android.content.Intent;

import com.ds.sapling.hookdemo.LoginActivity;

public class MainPresenter {

    public void getUser(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
