package com.project.tathanhson.wallpaperandringtons.view.activity

import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityDetailBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.detail.DetailFavoriteFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.DetailLiveWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.DetailRingtonesFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {


    override fun initViewModel() {

    }

    override fun initData() {
        val key = intent.getStringExtra(MainActivity.KEY_EXTRA)

        when (key) {
            MainActivity.KEY_WALLPAPER -> showFragment(DetailWallpaperFragment())
            MainActivity.KEY_RINGTONE -> showFragment(DetailRingtonesFragment())
            MainActivity.KEY_LIVE_WALLPAPER -> showFragment(DetailLiveWallpaperFragment())
            MainActivity.KEY_FAVORITE -> showFragment(DetailFavoriteFragment())
        }
    }

    override fun initView() {

    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    companion object {
        val TAG = DetailActivity::class.java.name
    }

}

