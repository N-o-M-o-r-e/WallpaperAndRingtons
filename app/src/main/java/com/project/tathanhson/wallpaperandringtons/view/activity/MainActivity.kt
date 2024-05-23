package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityMainBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.list.MyFavoriteFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper.LiveWallpapersFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones.RingtonesFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.WallpaperFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.FavoriteVM
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var wallpaperVM: WallpaperVM
    private lateinit var ringtonesVM: RingtonesVM
    private lateinit var liveWallpaperVM: LiveWallpaperVM
    private lateinit var favoriteVM: FavoriteVM

    private val frgWallpaper = WallpaperFragment()
    private val frgRingtone = RingtonesFragment()
    private val frgLiveWallpapers = LiveWallpapersFragment()
    private val frgFavorite = MyFavoriteFragment()

    override fun initViewModel() {
        wallpaperVM = ViewModelProvider(this)[WallpaperVM::class.java]
        ringtonesVM = ViewModelProvider(this)[RingtonesVM::class.java]
        liveWallpaperVM = ViewModelProvider(this)[LiveWallpaperVM::class.java]
        favoriteVM = ViewModelProvider(this)[FavoriteVM::class.java]
    }

    override fun initData() {
        CommonObject.itemWallpaper.observe(this, Observer { itemSelected ->
            itemSelected?.let {
                goToDetailActivity(KEY_WALLPAPER)
            }
        })

        CommonObject.positionDataRingtone.observe(this, Observer { position ->
            position?.let{
                goToDetailActivity(KEY_RINGTONE)
            }

        })

        CommonObject.itemLiveWallpaper.observe(this) { position ->
            position?.let {
                goToDetailActivity(KEY_LIVE_WALLPAPER)
            }
        }

        frgWallpaper.viewModel = wallpaperVM
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frgWallpaper).commit()
    }

    override fun initView() {

        binding.btnWallpaper.setOnClickListener {
            frgWallpaper.viewModel = wallpaperVM
            showFragmemnt(WallpaperFragment())
        }

        binding.btnRingtones.setOnClickListener {
            frgRingtone.viewModel = ringtonesVM
            showFragmemnt(RingtonesFragment())
        }

        binding.btnLiveWallpaper.setOnClickListener {
            frgLiveWallpapers.viewModel = liveWallpaperVM
            showFragmemnt(LiveWallpapersFragment())
        }

        binding.btnFavorite.setOnClickListener {
            frgFavorite.viewModel = favoriteVM
            showFragmemnt(MyFavoriteFragment())
        }
    }

    fun goToDetailActivity(key: String) {
        intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(KEY_EXTRA, key)
        startActivity(intent)
    }

    fun showFragmemnt(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    companion object{
        val TAG = MainActivity::class.java.name
        const val KEY_EXTRA = "KEY_EXTRA"
        const val KEY_WALLPAPER = "KEY_WALLPAPER"
        const val KEY_RINGTONE = "KEY_RINGTONE"
        const val KEY_LIVE_WALLPAPER = "KEY_LIVE_WALLPAPER"
        const val KEY_FAVORITE = "KEY_FAVORITE"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        finish()
    }
}