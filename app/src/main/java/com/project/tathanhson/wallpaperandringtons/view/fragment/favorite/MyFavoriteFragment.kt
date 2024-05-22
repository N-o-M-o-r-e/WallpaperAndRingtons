package com.project.tathanhson.wallpaperandringtons.view.fragment.favorite

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentMyFavoriteBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.FavLiveWallpaperAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.FavRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.FavWallpapersAdapter
import com.project.tathanhson.wallpaperandringtons.viewmodel.FavoriteVM

class MyFavoriteFragment :
    BaseFragment<FragmentMyFavoriteBinding>(FragmentMyFavoriteBinding::inflate) {
    lateinit var viewModel: FavoriteVM
    private lateinit var adapterWallpaperfav: FavWallpapersAdapter
    private lateinit var adapterRingtones: FavRingtonesAdapter
    private lateinit var adapterLiveWallpaper: FavLiveWallpaperAdapter

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FavoriteVM::class.java]
        viewModel.getListFavWallpaperIds()
        viewModel.fetchAllFavWallpapers()
        viewModel.fetchAllFavoriteRingtones()
        viewModel.fetchAllFavLiveWallpapers()
    }

    override fun initData() {
        createRecyclerViewWallpaper()
        createRecyclerViewRingtones()
        createRecyclerViewLiveWallpaper()
    }

    private fun createRecyclerViewWallpaper() {
        adapterWallpaperfav = FavWallpapersAdapter(mContext, ArrayList())
        binding.iclWallpapers.rcvWallpaperFav.adapter = adapterWallpaperfav
        binding.iclWallpapers.rcvWallpaperFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.favoriteWallpapers.observe(viewLifecycleOwner) { wallpapers ->
            adapterWallpaperfav.updateWallpapers(wallpapers)
        }
    }

    private fun createRecyclerViewRingtones() {
        adapterRingtones = FavRingtonesAdapter(mContext, ArrayList())
        binding.iclRingtones.rcvRingtonesFav.adapter = adapterRingtones
        binding.iclRingtones.rcvRingtonesFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.favoriteRingtones.observe(viewLifecycleOwner) { ringtones ->
            adapterRingtones.updateRingtones(ringtones)
        }
    }

    private fun createRecyclerViewLiveWallpaper() {
        adapterLiveWallpaper = FavLiveWallpaperAdapter(mContext, ArrayList())
        binding.iclLiveWallpapers.rcvLiveWallpaperFav.adapter = adapterLiveWallpaper
        binding.iclLiveWallpapers.rcvLiveWallpaperFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.favoriteLiveWallpapers.observe(viewLifecycleOwner) { liveWallpapers ->
            adapterLiveWallpaper.updateLiveWallpapers(liveWallpapers)
        }
    }

    override fun initView() {
        // Initialize any other views here if needed
    }
}