package com.demo.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.module_sdk.R

class TestActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sdk_test_activity)
    }
}