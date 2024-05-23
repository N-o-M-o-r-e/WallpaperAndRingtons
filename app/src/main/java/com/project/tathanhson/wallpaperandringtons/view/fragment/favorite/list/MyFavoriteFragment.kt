package com.project.tathanhson.wallpaperandringtons.view.fragment.favorite.list

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentMyFavoriteBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.ListFavoriteActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list.FavLiveWallpaperAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list.FavRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list.FavWallpapersAdapter
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

    @SuppressLint("SuspiciousIndentation")
    private fun createRecyclerViewWallpaper() {
        adapterWallpaperfav = FavWallpapersAdapter(mContext, ArrayList())
        binding.iclWallpapers.rcvWallpaperFav.adapter = adapterWallpaperfav
        binding.iclWallpapers.rcvWallpaperFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            CommonObject.favoriteWallpapers.observe(viewLifecycleOwner) { wallpapers ->
            adapterWallpaperfav.updateWallpapers(wallpapers)
        }
    }

    private fun createRecyclerViewRingtones() {
        adapterRingtones = FavRingtonesAdapter(mContext, ArrayList())
        binding.iclRingtones.rcvRingtonesFav.adapter = adapterRingtones
        binding.iclRingtones.rcvRingtonesFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        CommonObject.favoriteRingtones.observe(viewLifecycleOwner) { ringtones ->
            adapterRingtones.updateRingtones(ringtones)
        }
    }

    private fun createRecyclerViewLiveWallpaper() {
        adapterLiveWallpaper = FavLiveWallpaperAdapter(mContext, ArrayList())
        binding.iclLiveWallpapers.rcvLiveWallpaperFav.adapter = adapterLiveWallpaper
        binding.iclLiveWallpapers.rcvLiveWallpaperFav.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        CommonObject.favoriteLiveWallpapers.observe(viewLifecycleOwner) { liveWallpapers ->
            adapterLiveWallpaper.updateLiveWallpapers(liveWallpapers)
        }
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
        val intent = Intent(requireActivity(), ListFavoriteActivity::class.java)
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