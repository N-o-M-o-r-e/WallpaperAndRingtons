package com.example.wallpagerandringtons.model.repository

import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {

    @GET("wallpapers/category/{categoryId}/?limit=100")
    fun getWallpaperList(@Path("categoryId") categoryId: Int): Call<WallpaperList>

    @GET("wallpapers/category/{categoryId}/")
    fun getWallpaperList(
        @Path("categoryId") categoryId: Int,
        @Query("limit") limit: Int
    ): Call<WallpaperList>
}