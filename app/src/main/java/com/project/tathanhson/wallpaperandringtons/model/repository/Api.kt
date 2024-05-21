package com.example.wallpagerandringtons.model.repository

import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpapers
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Categories
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {
    @GET("wallpapers/category/{categoryId}/")
    fun getWallpaperList(
        @Path("categoryId") categoryId: Int,
        @Query("limit") limit: Int
    ): Call<Wallpapers>

    @GET("category/")
    fun getWallpaperTitle(): Call<Categories>

    @POST("wallpapers/{id}/updatefavorite")
    fun postUpdateFavorite(@Path("id") wallpaperId: Int): Call<WallpaperItem>

    @POST("wallpapers/{id}/updatedownload")
    fun postUpdateDownload(@Path("id") wallpaperId: Int): Call<WallpaperItem>

    @GET("wallpapers/category/{liveWallpaperId}")
    fun getLiveWallpapers(
        @Path("liveWallpaperId") categoryId: Int
    ) : Call<LiveWallpapers>
}