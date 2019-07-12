package com.ds.sapling.hookdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ds.sapling.hookdemo.permission.annotation.PermissionCancel;
import com.ds.sapling.hookdemo.permission.annotation.PermissionDenied;
import com.ds.sapling.hookdemo.permission.annotation.RequestPermission;

import java.util.List;

/**
 * 测试6.0权限管理
 */
public class LoginActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    @RequestPermission(value = {Manifest.permission.READ_CONTACTS},requestCode = 100)
    public void attemptLogin() {
        Toast.makeText(this, "start login", Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel(requestCode = 100)
    public void permissionCanceled(List<String> permissions){
        Toast.makeText(this, "permission cancel"+permissions.size(), Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 100)
    public void permissionDenied(List<String> permissions){
        Toast.makeText(this, "permission denied"+permissions.size(), Toast.LENGTH_SHORT).show();
    }

}

