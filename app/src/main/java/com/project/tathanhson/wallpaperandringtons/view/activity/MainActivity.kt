package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.DetailFavoriteFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.DetailLiveWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.RingtonesFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.WallpaperFragment
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityMainBinding
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM

class MainActivity : BaseActivity<ActivityMainBinding>(){


    private lateinit var wallpaperVM: WallpaperVM
    private lateinit var ringtonesVM: RingtonesVM
    private val frgWallpaper = WallpaperFragment()
    private val frgRingtone = RingtonesFragment()
    companion object {
        val TAG = MainActivity::class.java.name
    }

    override fun initViewModel() {
        wallpaperVM = ViewModelProvider(this)[WallpaperVM::class.java]
        ringtonesVM = ViewModelProvider(this)[RingtonesVM::class.java]
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
        initData()
        checkInternet()
    }

    private fun initData() {
        wallpaperVM.ldItemWallpaper.observe(this, Observer { item ->
            goToDetailActivity(item)
        })
    }
    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    fun goToDetailActivity(item: WallpaperItem?) {
        intent = Intent(this, DetailActivity::class.java)
        CommonObject.iamgeWallperLD.value = item
        startActivity(intent)
    }


    private fun checkInternet(){

    }
}