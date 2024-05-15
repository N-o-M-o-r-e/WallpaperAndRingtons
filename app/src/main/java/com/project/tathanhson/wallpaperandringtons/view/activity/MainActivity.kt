package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.example.wallpagerandringtons.view.fragment.detail.DetailFavoriteFragment
import com.example.wallpagerandringtons.view.fragment.detail.DetailLiveWallpaperFragment
import com.example.wallpagerandringtons.view.fragment.list.RingtonesFragment
import com.example.wallpagerandringtons.view.fragment.list.WallpaperFragment
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(){

    private lateinit var viewModel: WallpaperMV
    private val frgWallpaper = WallpaperFragment()
    companion object {
        val TAG = MainActivity::class.java.name
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[WallpaperMV::class.java]
    }

    override fun initView() {
        frgWallpaper.viewModel = viewModel
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frgWallpaper).commit()


        binding.btnWallpaper.setOnClickListener {
            frgWallpaper.viewModel = viewModel
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frgWallpaper).commit()
        }

        binding.btnRingtones.setOnClickListener {
            showFragmemnt(RingtonesFragment())
        }

        binding.btnLiveWallpaper.setOnClickListener {
            showFragmemnt(DetailLiveWallpaperFragment())
        }

        binding.btnFavorite.setOnClickListener {
            showFragmemnt(DetailFavoriteFragment())
        }
        initData()
    }

    private fun initData() {
        viewModel.ldItemWallpaper.observe(this, Observer { item ->
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
}