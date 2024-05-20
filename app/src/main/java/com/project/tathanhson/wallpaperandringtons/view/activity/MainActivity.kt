package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.DetailFavoriteFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.DetailLiveWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.RingtonesFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.WallpaperFragment
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityMainBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var wallpaperVM: WallpaperVM
    private lateinit var ringtonesVM: RingtonesVM
    private val frgWallpaper = WallpaperFragment()
    private val frgRingtone = RingtonesFragment()

    override fun initViewModel() {
        wallpaperVM = ViewModelProvider(this)[WallpaperVM::class.java]
        ringtonesVM = ViewModelProvider(this)[RingtonesVM::class.java]
    }

    override fun initData() {
        wallpaperVM.ldItemWallpaper.observe(this, Observer { item ->
            goToDetailWallpaper(item)
        })

        CommonObject.positionDataRingtone.observe(this, Observer { position ->
            goToDetailRingtone()
        })

    }

    override fun initView() {
        frgWallpaper.viewModel = wallpaperVM
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frgWallpaper).commit()


        binding.btnWallpaper.setOnClickListener {
            frgWallpaper.viewModel = wallpaperVM
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frgWallpaper).commit()
        }

        binding.btnRingtones.setOnClickListener {
            frgRingtone.viewModel = ringtonesVM
            showFragmemnt(RingtonesFragment())
        }

        binding.btnLiveWallpaper.setOnClickListener {
            showFragmemnt(DetailLiveWallpaperFragment())
        }

        binding.btnFavorite.setOnClickListener {
            showFragmemnt(DetailFavoriteFragment())
        }
    }

    fun goToDetailWallpaper(item: WallpaperItem?) {
        intent = Intent(this, DetailActivity::class.java)
        CommonObject.iamgeWallperLD.value = item
        intent.putExtra(KEY_WALLPAPER, 0)
        startActivity(intent)
    }

    fun goToDetailRingtone() {
        intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(KEY_RINGTONE, 1)
        startActivity(intent)
    }

    fun showFragmemnt(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    companion object{
        val TAG = MainActivity::class.java.name
        const val KEY_WALLPAPER = "KEY_WALLPAPER"
        const val KEY_RINGTONE = "KEY_RINGTONE"
        const val KEY_LIVE_WALLPAPER = "KEY_LIVE_WALLPAPER"
        const val KEY_FAVORITE = "KEY_FAVORITE"
    }
}