package com.project.tathanhson.wallpaperandringtons.view.activity

import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityDetailBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.DetailRingtoneFavFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.DetailLiveWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.DetailRingtonesFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment

/**
 * Show detail Item Wallpaper / Ringtone / LiveWallpaper
 */

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {


    override fun initViewModel() {

    }

    override fun initData() {
        val key = intent.getStringExtra(MainActivity.KEY_EXTRA)

        when (key) {
            MainActivity.KEY_WALLPAPER -> showFragment(DetailWallpaperFragment())
            MainActivity.KEY_RINGTONE -> showFragment(DetailRingtonesFragment())
            MainActivity.KEY_LIVE_WALLPAPER -> showFragment(DetailLiveWallpaperFragment())
        }

        val keyFav = intent.getStringExtra(FavoriteActivity.keyFav)
        when (keyFav){
            FavoriteActivity.KEY_RINGTONE ->showFragment(DetailRingtoneFavFragment())
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

