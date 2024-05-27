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
@SuppressLint("LogConditional")
class FavoriteVM : ViewModel() {

    val sharedPreferencesWallpaper = SharedPreferencesWallpaper()
    val sharedPreferencesRingtones = SharedPreferencesRingtones()
    val sharedPreferencesLiveWallpaper = SharedPreferencesLiveWallpaper()

    fun fetchAllFavWallpapers() {
        val wallpaperIds = sharedPreferencesWallpaper.getWallpapers()
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

    fun fetchAllFavLiveWallpapers() {
        val liveWallpaperIds = sharedPreferencesLiveWallpaper.getWallpapers()
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

    fun fetchAllFavoriteRingtones() {
        val ringtoneUrl = sharedPreferencesRingtones.getRingtones()
        CommonObject._favoriteRingtones.postValue(ringtoneUrl)
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
}