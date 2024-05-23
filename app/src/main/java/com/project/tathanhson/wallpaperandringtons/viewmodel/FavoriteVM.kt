package com.project.tathanhson.wallpaperandringtons.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallpagerandringtons.model.repository.Api
import com.example.wallpagerandringtons.model.repository.RetrofitHelper
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesLiveWallpaper
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesRingtones
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesWallpaper
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpapers
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteVM : ViewModel() {

    val sharedPreferencesWallpaper = SharedPreferencesWallpaper()
    val sharedPreferencesRingtones = SharedPreferencesRingtones()
    val sharedPreferencesLiveWallpaper = SharedPreferencesLiveWallpaper()



    @SuppressLint("LogConditional")
    fun getListFavWallpaperIds(): List<Int> {
        val wallpaperIds = sharedPreferencesWallpaper.getWallpapers()
        Log.d("AAAAAAAAAA", "getListFavWallpaperIds: $wallpaperIds")
        return wallpaperIds
    }

    fun getListFavLiveWallpaperIds(): List<Int> {
        val liveWallpaperIds = sharedPreferencesLiveWallpaper.getWallpapers()
        Log.d("AAAAAAAAAA", "getListFavLiveWallpaperIds: $liveWallpaperIds")
        return liveWallpaperIds
    }

    private fun getWallpaperFav(id_wallpaper: Int, onResult: (WallpaperItem?) -> Unit) {
        val favWallpaper = RetrofitHelper.getInstance().create(Api::class.java)

        favWallpaper.getWallpaperById(id_wallpaper).enqueue(object : Callback<WallpaperItem> {
            override fun onResponse(call: Call<WallpaperItem>, response: Response<WallpaperItem>) {
                val favWallpaper = response.body()
                Log.e("AAAAAAAAA", "Wallpaper By ID: $favWallpaper")
                onResult(favWallpaper)
            }

            override fun onFailure(call: Call<WallpaperItem>, t: Throwable) {
                Log.e("AAAAAAAAA", "onFailure: ${t.message}")
                onResult(null)
            }
        })
    }

    private fun getLiveWallpaperFav(id_wallpaper: Int, onResult: (LiveWallpaperItem?) -> Unit) {
        val favLiveWallpaper = RetrofitHelper.getInstance().create(Api::class.java)

        favLiveWallpaper.getLiveWallpaperById(id_wallpaper).enqueue(object : Callback<LiveWallpaperItem> {
            @SuppressLint("LogConditional")
            override fun onResponse(
                call: Call<LiveWallpaperItem>,
                response: Response<LiveWallpaperItem>
            ) {
                val favLiveWallpaper = response.body()
                Log.d("AAAAAAAAAA", "Live Wallpaper body: $favLiveWallpaper")
                onResult(favLiveWallpaper)
            }

            override fun onFailure(call: Call<LiveWallpaperItem>, t: Throwable) {
                Log.e("AAAAAAAAA", "onFailure: ${t.message}")
                onResult(null)
            }
        })
    }

    // Tìm nạp tất cả Favorite Wallpaper lưu trong sharedPreferences
    fun fetchAllFavWallpapers() {
        val wallpaperIds = getListFavWallpaperIds()
        val wallpapers = Wallpapers()

        for (id in wallpaperIds) {
            getWallpaperFav(id) { wallpaperItem ->
                wallpaperItem?.let {
                    wallpapers.add(it)
                }
                if (wallpapers.size == wallpaperIds.size) {
                    CommonObject._favoriteWallpapers.postValue(wallpapers)
                }
            }
        }
    }

    fun fetchAllFavoriteRingtones() {
        val ringtoneUrl = sharedPreferencesRingtones.getRingtones()
        Log.d("AAAAAAAAAA", "getListFavRingtoneUrl: $ringtoneUrl")
        CommonObject._favoriteRingtones.postValue(ringtoneUrl)
    }

    fun fetchAllFavLiveWallpapers() {
        val liveWallpaperIds = getListFavLiveWallpaperIds()
        val liveWallpapers = ArrayList<LiveWallpaperItem>()

        for (id in liveWallpaperIds) {
            getLiveWallpaperFav(id) { wallpaperItem ->
                wallpaperItem?.let {
                    liveWallpapers.add(it)
                }
                if (liveWallpapers.size == liveWallpaperIds.size) {
                    CommonObject._favoriteLiveWallpapers.postValue(liveWallpapers)
                }
            }
        }
    }
}