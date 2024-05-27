package com.project.tathanhson.wallpaperandringtons.view.fragment.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentMyFavoriteBinding
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.activity.FavoriteActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list.FavLiveWallpaperAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list.FavRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list.FavWallpapersAdapter
import com.project.tathanhson.wallpaperandringtons.viewmodel.FavoriteVM

class FavoriteFragment :
    BaseFragment<FragmentMyFavoriteBinding>(FragmentMyFavoriteBinding::inflate) {
    lateinit var viewModel: FavoriteVM
    private lateinit var adapterWallpaperfav: FavWallpapersAdapter
    private lateinit var adapterRingtones: FavRingtonesAdapter
    private lateinit var adapterLiveWallpaper: FavLiveWallpaperAdapter

    private var listRingtones = ArrayList<Data>()
    private var listWallpaper = ArrayList<WallpaperItem>()
    private var listLiveWallpaper = ArrayList<LiveWallpaperItem>()

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity())[FavoriteVM::class.java]
        viewModel.fetchAllFavWallpapers()
        viewModel.fetchAllFavoriteRingtones()
        viewModel.fetchAllFavLiveWallpapers()
    }

    override fun initViewModel() {
        binding.iclWallpapers.root.visibility = View.GONE
        binding.iclRingtones.root.visibility = View.GONE
        binding.iclLiveWallpapers.root.visibility = View.GONE
        binding.warning.visibility = View.VISIBLE

    }

    private fun checkView() {
        if (listWallpaper.isEmpty() && listRingtones.isEmpty() && listLiveWallpaper.isEmpty()) {
            binding.warning.visibility = View.VISIBLE
        } else {
            if (listWallpaper.isNotEmpty() || listRingtones.isNotEmpty() || listLiveWallpaper.isNotEmpty()) {
                binding.warning.visibility = View.GONE
            }
        }
    }

    override fun initData() {
        CommonObject.favoriteWallpapers.observe(viewLifecycleOwner) { wallpapers ->
            listWallpaper = wallpapers
            checkView()
            Log.e("FFFFFFFF", "initData: Wallpaper " + wallpapers)
            if (wallpapers != null && wallpapers.isNotEmpty()) {
                createRecyclerViewWallpaper(wallpapers)
                binding.iclWallpapers.root.visibility = View.VISIBLE
            } else {
                binding.iclWallpapers.root.visibility = View.GONE
            }
        }

        CommonObject.favoriteRingtones.observe(viewLifecycleOwner) { ringtones ->
            listRingtones = ringtones
            checkView()
            Log.e("FFFFFFFF", "initData: Ringtone " + ringtones)
            if (ringtones != null && ringtones.isNotEmpty()) {
                createRecyclerViewRingtones(ringtones)
                binding.iclRingtones.root.visibility = View.VISIBLE
            } else {
                binding.iclRingtones.root.visibility = View.GONE
            }
        }

        CommonObject.favoriteLiveWallpapers.observe(viewLifecycleOwner) { liveWallpapers ->
            listLiveWallpaper = liveWallpapers
            checkView()
            Log.e("FFFFFFFF", "initData: LiveWallpaper " + liveWallpapers)
            if (liveWallpapers != null && liveWallpapers.isNotEmpty()) {
                createRecyclerViewLiveWallpaper(liveWallpapers)
                binding.iclLiveWallpapers.root.visibility = View.VISIBLE
            } else {
                binding.iclLiveWallpapers.root.visibility = View.GONE
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun createRecyclerViewWallpaper(wallpapers: ArrayList<WallpaperItem>) {
        adapterWallpaperfav = FavWallpapersAdapter(mContext, ArrayList())
        binding.iclWallpapers.rcvWallpaperFav.adapter = adapterWallpaperfav
        binding.iclWallpapers.rcvWallpaperFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapterWallpaperfav.updateWallpapers(wallpapers)

    }

    @SuppressLint("SuspiciousIndentation")
    private fun createRecyclerViewRingtones(ringtones: ArrayList<Data>) {
        adapterRingtones = FavRingtonesAdapter(mContext, ArrayList())
        binding.iclRingtones.rcvRingtonesFav.adapter = adapterRingtones
        binding.iclRingtones.rcvRingtonesFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapterRingtones.updateRingtones(ringtones)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createRecyclerViewLiveWallpaper(liveWallpapers: ArrayList<LiveWallpaperItem>) {
        adapterLiveWallpaper = FavLiveWallpaperAdapter(mContext, ArrayList())
        binding.iclLiveWallpapers.rcvLiveWallpaperFav.adapter = adapterLiveWallpaper
        binding.iclLiveWallpapers.rcvLiveWallpaperFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapterLiveWallpaper.updateLiveWallpapers(liveWallpapers)
    }


    override fun initView() {


        binding.iclWallpapers.tvSeeMore.setOnClickListener {
            goToListFavActivity(KEY_WALLPAPER_FAV)
        }
        binding.iclRingtones.tvSeeMore.setOnClickListener {
            goToListFavActivity(KEY_RINGTONE_FAV)
        }
        binding.iclLiveWallpapers.tvSeeMore.setOnClickListener {
            goToListFavActivity(KEY_LIVE_WALLPAPER_FAV)
        }
    }


    fun goToListFavActivity(key: String) {
        val intent = Intent(requireActivity(), FavoriteActivity::class.java)
        intent.putExtra(KEY_EXTRA_FRG, key)
        startActivity(intent)
    }

    companion object{
        const val KEY_EXTRA_FRG = "KEY_WALLPAPER_FAV"
        const val KEY_WALLPAPER_FAV = "KEY_WALLPAPER_FAV"
        const val KEY_RINGTONE_FAV = "KEY_RINGTONE_FAV"
        const val KEY_LIVE_WALLPAPER_FAV = "KEY_LIVE_WALLPAPER_FAV"
    }
}