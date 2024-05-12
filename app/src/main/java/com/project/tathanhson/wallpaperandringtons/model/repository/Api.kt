package com.example.wallpagerandringtons.model.repository

import com.example.wallpagerandringtons.model.WallpaperList
import retrofit2.Call
import retrofit2.http.GET


interface Api {

    @GET("wallpapers/?skip=0&limit=15")
    fun getWallpaperList(): Call<WallpaperList>
}