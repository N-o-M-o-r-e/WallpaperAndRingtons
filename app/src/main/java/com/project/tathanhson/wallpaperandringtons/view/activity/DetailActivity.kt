package com.project.tathanhson.wallpaperandringtons.view.activity

import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityDetailBinding
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment

class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    companion object {
        val TAG = DetailActivity::class.java.name
    }

    override fun initViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun initViewModel() {

    }

    override fun initView() {
        val fragment = DetailWallpaperFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

}

