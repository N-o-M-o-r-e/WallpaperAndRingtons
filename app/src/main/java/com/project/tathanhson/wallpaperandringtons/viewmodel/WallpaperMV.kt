package com.example.wallpagerandringtons.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
import com.example.wallpagerandringtons.model.repository.Api
import com.example.wallpagerandringtons.model.repository.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperMV : ViewModel() {
    var ldListWallpaper = MutableLiveData<WallpaperList?>()
    var ldItemWallpaper = MutableLiveData<WallpaperItem?>()


    fun getAPI() {
        val listWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        listWallpaper.getWallpaperList(8,100).enqueue(object : Callback<WallpaperList> {
            override fun onResponse(call: Call<WallpaperList>, response: Response<WallpaperList>) {
                val listWallpaper = response.body()
                listWallpaper?.let {
                    ldListWallpaper.value = listWallpaper
                    Log.e("AAAAAAAAAAAAAAAAAAAAA", "onResponse: "+listWallpaper, )
                }
            }

            override fun onFailure(call: Call<WallpaperList>, t: Throwable) {
                Log.e("AAAAAAAAAAAAAAAAAAAAA", "onFailure: " + t.message)
            }
        })
    }


}



