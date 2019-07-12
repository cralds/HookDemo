package com.ds.sapling.hookdemo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ds.sapling.hookdemo.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_tvHello.setOnClickListener {
            Toast.makeText(this,"click hello",Toast.LENGTH_LONG).show()
            MainPresenter().getUser(this)
        }
        HookClickListener.startHook(this,main_tvHello)
    }
}
