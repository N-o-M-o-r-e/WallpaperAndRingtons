package com.project.tathanhson.wallpaperandringtons.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallpagerandringtons.model.repository.Api
import com.example.wallpagerandringtons.model.repository.RetrofitHelper
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpapers
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveWallpaperVM : ViewModel() {
    var itemLiveWallpaper = MutableLiveData<LiveWallpaperItem>()

    fun getApiLiveWallpaper(liveWallpaperId: Int) {
        val liveWallpapers = RetrofitHelper.getInstance().create(Api::class.java)

        liveWallpapers.getLiveWallpapers(liveWallpaperId).enqueue(object : Callback<LiveWallpapers> {
            override fun onResponse(
                call: Call<LiveWallpapers>,
                response: Response<LiveWallpapers>
            ) {
                val listLiveWallpapers = response.body()
                listLiveWallpapers?.let {
                    CommonObject.listLiveWallpapers.value = listLiveWallpapers
                }
            }

            override fun onFailure(call: Call<LiveWallpapers>, t: Throwable) {
                Log.e("AAAAAAAAAAAAA", "onFailure LiveWallpaper: "+ t.message, )
            }
        })
    }

    fun postUpdateFavorite(wallpaperId: Int) {
        val favoriteWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        favoriteWallpaper.postUpdateFavorite(wallpaperId).enqueue(object : Callback<WallpaperItem> {
            override fun onResponse(call: Call<WallpaperItem>, response: Response<WallpaperItem>) {
                response.body()
            }
            override fun onFailure(call: Call<WallpaperItem>, t: Throwable) {

            }
        })
    }
}