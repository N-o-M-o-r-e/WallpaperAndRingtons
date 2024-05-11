package com.example.wallpagerandringtons.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallpagerandringtons.model.WallpaperList
import com.example.wallpagerandringtons.model.repository.Api
import com.example.wallpagerandringtons.model.repository.RetrofitHelper
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperMV : ViewModel() {
    var ldListWallpaper = MutableLiveData<WallpaperList?>()

    fun getAPI() {
        val listWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        listWallpaper.getWallpaperList().enqueue(object : Callback<WallpaperList> {
            override fun onResponse(call: Call<WallpaperList>, response: Response<WallpaperList>) {
                val listWallpaper = response.body()
                listWallpaper?.let {
                    ldListWallpaper.value = listWallpaper
                    Log.d("AAAAAAAA", "onResponse: $listWallpaper")
                }
            }

            override fun onFailure(call: Call<WallpaperList>, t: Throwable) {
                Log.e("API", "onFailure: " + t.message)
            }
        })
    }

}



