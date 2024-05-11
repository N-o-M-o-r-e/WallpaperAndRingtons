package com.project.tathanhson.wallpaperandringtons.view.activity

import com.example.wallpagerandringtons.view.fragment.detail.DetailWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityMainBinding

class HomeActivity : BaseActivity<ActivityMainBinding>() {


    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        showFragmemnt()

    }

    private fun showFragmemnt() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, DetailWallpaperFragment())
            .commit()
    }

}