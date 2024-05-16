package com.example.wallpagerandringtons.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallpagerandringtons.model.repository.Api
import com.example.wallpagerandringtons.model.repository.RetrofitHelper
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.ListTitle
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Title
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperVM : ViewModel() {
    var ldListWallpaper = MutableLiveData<WallpaperList?>()
    var ldItemWallpaper = MutableLiveData<WallpaperItem?>()
    var ldListTitle = MutableLiveData<ListTitle?>()
    var ldItemTitle = MutableLiveData<Title?>()


    fun getApiWallpaperDetail(categoryId: Int) {
        val listWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        listWallpaper.getWallpaperList(categoryId,100).enqueue(object : Callback<WallpaperList> {
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

    fun getApiWallpaperTitle(){
        val listTitle = RetrofitHelper.getInstance().create(Api::class.java)
        listTitle.getWallpaperTitle().enqueue(object  : Callback<ListTitle> {
            override fun onResponse(
                call: Call<ListTitle>,
                response: Response<ListTitle>
            ) {
                val listTitleWallpaper = response.body()
                listTitleWallpaper?.let {
                    ldListTitle.value = listTitleWallpaper
                    ldItemTitle.value = listTitleWallpaper[0]
                        Log.e("AAAAAAAAAAAAAAAAAAAAA", "onResponse: "+listTitleWallpaper )
                }
            }

            override fun onFailure(call: Call<ListTitle>, t: Throwable) {
                Log.e("AAAAAAAAAAAAAAAAAAAAA", "onFailure: "+t.message )
            }
        } )
    }

    fun postUpdateFavorite(wallpaperId: Int) {
        val favoriteWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        favoriteWallpaper.postUpdateFavorite(wallpaperId).enqueue(object : Callback<WallpaperItem> {
            override fun onResponse(call: Call<WallpaperItem>, response: Response<WallpaperItem>) {
                Log.d("AAAAAAAAAAAAA", "onResponse: Update success: " + response.body())
                response.body()
            }

            override fun onFailure(call: Call<WallpaperItem>, t: Throwable) {
                Log.d("AAAAAAAAAAAAA", "onFailure: " + t.message)
            }

        })

    }

    fun postUpdateDownload(wallpaperId: Int) {
        val downloadsWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        downloadsWallpaper.postUpdateFavorite(wallpaperId)
            .enqueue(object : Callback<WallpaperItem> {
                override fun onResponse(
                    call: Call<WallpaperItem>,
                    response: Response<WallpaperItem>
                ) {
                    Log.d("AAAAAAAAAAAAA", "onResponse: download success: " + response.body())
                    response.body()
                }

                override fun onFailure(call: Call<WallpaperItem>, t: Throwable) {
                    Log.d("AAAAAAAAAAAAA", "onFailure: " + t.message)
                }

            })

    }

}



