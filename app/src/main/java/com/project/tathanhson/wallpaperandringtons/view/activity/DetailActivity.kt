package com.project.tathanhson.wallpaperandringtons.view.activity

import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityDetailBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.DetailFavoriteFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.DetailLiveWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.DetailRingtonesFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {
    override fun initViewModel() {

    }

    override fun initData() {

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

    fun showFragmemnt(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    companion object {
        val TAG = DetailActivity::class.java.name
    }

}

