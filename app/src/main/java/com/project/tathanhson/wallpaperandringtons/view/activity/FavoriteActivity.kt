package com.project.tathanhson.wallpaperandringtons.view.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallpagerandringtons.view.adapter.wallpapers.ListWallpaperAdapter
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityListFavoriteBinding
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpapers
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.detail.FavRingtoneListAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.live_wallpaper.LiveWallpaperAdapter
import com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.FavoriteFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.FavoriteVM
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM

class FavoriteActivity :
    BaseActivity<ActivityListFavoriteBinding>(ActivityListFavoriteBinding::inflate) {
    private var index = 0

    lateinit var viewModel: FavoriteVM

    lateinit var wallpaperVM: WallpaperVM
    lateinit var ringtonesVM: RingtonesVM
    lateinit var liveWallpapersVM : LiveWallpaperVM

    private var adapterWallpapers: ListWallpaperAdapter? = null
    private var adapterRingtones: FavRingtoneListAdapter? = null
    private var adapterLiveWallpapers: LiveWallpaperAdapter? = null

    private var listWallpaper: ArrayList<WallpaperItem>? = null

    private var listLiveWallpaper : LiveWallpapers? = null

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this)[FavoriteVM::class.java]
        viewModel.fetchAllFavWallpapers()
        viewModel.fetchAllFavoriteRingtones()
        viewModel.fetchAllFavLiveWallpapers()
    }

    override fun initViewModel() {
        wallpaperVM = ViewModelProvider(this)[WallpaperVM::class.java]
        ringtonesVM = ViewModelProvider(this)[RingtonesVM::class.java]
        liveWallpapersVM = ViewModelProvider(this)[LiveWallpaperVM::class.java]
    }

    override fun initData() {
        CommonObject.itemWallpaper.observe(this, Observer { itemSelected ->
            itemSelected?.let {
                goToDetailActivity(MainActivity.KEY_WALLPAPER)
            }
        })

        CommonObject.positionDataRingtone.observe(this, Observer { position ->
            position?.let{
                goToDetailActivity(MainActivity.KEY_RINGTONE)
            }

        })

        CommonObject.itemLiveWallpaper.observe(this) { position ->
            position?.let {
                goToDetailActivity(MainActivity.KEY_LIVE_WALLPAPER)
            }
        }


        val key = intent.getStringExtra(FavoriteFragment.KEY_EXTRA_FRG)

        when (key) {
            FavoriteFragment.KEY_WALLPAPER_FAV -> {
                Toast.makeText(this, "Wallpaper", Toast.LENGTH_SHORT).show()
                index = 1
            }

            FavoriteFragment.KEY_RINGTONE_FAV -> {
                Toast.makeText(this, "Ringtones", Toast.LENGTH_SHORT).show()
                index = 2
            }

            FavoriteFragment.KEY_LIVE_WALLPAPER_FAV -> {
                Toast.makeText(this, "Live Wallpaper", Toast.LENGTH_SHORT).show()
                index = 3
            }
        }

        listenerData()
    }

    private fun listenerData() {
        CommonObject.positionRingtoneFav.observe(this){ itemRingtone ->
            itemRingtone?.let {
                intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(FavoriteActivity.keyFav, KEY_RINGTONE)
                startActivity(intent)
            }
        }
    }

    override fun initView() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            adapterRingtones?.let {
                adapterRingtones?.stopMediaRingtone()
            }

            finish()
        }
        when (index) {
            1 -> {
                viewWallpaper()
            }

            2 -> {
                viewRingtones()
            }

            3 -> {
                viewLiveWallpaper()
            }
        }
    }

    private fun viewWallpaper() {
        binding.title.text = getString(R.string.wallpapers)

        CommonObject.favoriteWallpapers.observe(this) { wallpaperList ->
            wallpaperList?.let {
                Log.d("CCCCCCCC", "viewWallpaper: $wallpaperList")
                listWallpaper = wallpaperList
                adapterWallpapers = ListWallpaperAdapter(this, wallpaperVM, this, wallpaperList)
                val layoutManager = GridLayoutManager(this, 3)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = adapterWallpapers
            }
        }
    }


    fun goToDetailActivity(key: String) {
        intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(MainActivity.KEY_EXTRA, key)
        startActivity(intent)
    }

    private fun viewRingtones() {
        binding.title.text = getString(R.string.ringtones)

        CommonObject.favoriteRingtones.observe(this) { listRingtoneData ->
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            adapterRingtones = FavRingtoneListAdapter(this, ringtonesVM, this, ArrayList(), "")
            binding.recyclerView.adapter = adapterRingtones
            adapterRingtones!!.updateRingtones(listRingtoneData)
        }
    }

    private fun viewLiveWallpaper() {
        binding.title.text = getString(R.string.live_wallpapers)

        CommonObject.favoriteLiveWallpapers.observe(this) { liveWallpapers ->
            Log.e("CCCCCCCCCCCCCC", "viewLiveWallpaper: "+liveWallpapers )
            liveWallpapers?.let {
                adapterLiveWallpapers = LiveWallpaperAdapter(this, liveWallpapersVM, this, it )
                val layoutManager = GridLayoutManager(this, 3)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = adapterLiveWallpapers
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        finish()
    }

    companion object{
        const val keyFav = "KEY_FAV"
        const val KEY_RINGTONE = "KEY_RINGTONE"
    }
}