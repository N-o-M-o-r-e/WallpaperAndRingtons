package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityDetailBinding
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.DetailFavoriteFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.DetailLiveWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.DetailRingtonesFragment
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
        val receivedValue = intent.getIntExtra(MainActivity.KEY_RINGTONE, 0)
        when (receivedValue){
            0-> showFragmemnt(DetailWallpaperFragment())
            1-> showFragmemnt(DetailRingtonesFragment())
            2-> showFragmemnt(DetailLiveWallpaperFragment())
            3-> showFragmemnt(DetailFavoriteFragment())
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
    }

}

