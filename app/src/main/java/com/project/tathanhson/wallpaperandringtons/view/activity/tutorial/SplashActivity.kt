package com.project.tathanhson.wallpaperandringtons.view.activity.tutorial

import android.content.Context
import android.content.Intent
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.ActivitySplashBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.MainActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.tutorial.onbroading.OnBroadAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val splashDelay: Long = 2000
    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_TIME_KEY = "first_time"

    override fun initViewModel() {
        // Initialize your ViewModel here if needed
    }

    override fun initData() {
        CommonObject.itemWallpaper.value = null
        CommonObject.positionDataRingtone.value = null
        CommonObject.itemLiveWallpaper.value = null
    }

    override fun initView() {
        // Initialize your views here if needed
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDelay)

            val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val isFirstTime = sharedPreferences.getBoolean(FIRST_TIME_KEY, true)

            if (isFirstTime) {
                startActivity(Intent(this@SplashActivity, OnBroadAct::class.java))
                sharedPreferences.edit().putBoolean(FIRST_TIME_KEY, false).apply()
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

            finish()
        }
    }
}
