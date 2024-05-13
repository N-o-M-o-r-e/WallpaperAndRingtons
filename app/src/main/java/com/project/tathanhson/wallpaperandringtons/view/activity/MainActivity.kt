package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wallpagerandringtons.model.WallpaperItem
import com.example.wallpagerandringtons.view.fragment.detail.DetailFavoriteFragment
import com.example.wallpagerandringtons.view.fragment.detail.DetailLiveWallpaperFragment
import com.example.wallpagerandringtons.view.fragment.detail.DetailRingtonesFragment
import com.example.wallpagerandringtons.view.fragment.list.WallpaperFragment
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(){

    private lateinit var viewModel: WallpaperMV
    companion object {
        val TAG = MainActivity::class.java.name
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[WallpaperMV::class.java]
    }

    override fun initView() {
        val fragment = WallpaperFragment()
        fragment.viewModel = viewModel
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()

        binding.btnWallpaper.setOnClickListener {
            fragment.viewModel = viewModel
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
        }

        binding.btnRingtones.setOnClickListener {
            showFragmemnt(DetailRingtonesFragment())
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