package com.project.tathanhson.wallpaperandringtons.view.activity.tutorial

import android.content.Intent
import com.project.tathanhson.wallpaperandringtons.databinding.ActivitySplashBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.tutorial.onbroading.OnBroadAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val splashDelay: Long = 2000

    override fun initViewModel() {

    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDelay)
            startActivity(Intent(this@SplashActivity, OnBroadAct::class.java))
            finish()
        }
    }

}