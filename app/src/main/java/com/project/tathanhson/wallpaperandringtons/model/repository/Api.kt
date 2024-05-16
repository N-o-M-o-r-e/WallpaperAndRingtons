package com.example.wallpagerandringtons.model.repository

import com.project.tathanhson.wallpaperandringtons.model.wallpaper.ListTitle
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
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
    ): Call<WallpaperList>

    @GET("category/")
    fun getWallpaperTitle(): Call<ListTitle>

    @POST("wallpapers/{id}/updatefavorite")
    fun postUpdateFavorite(@Path("id") wallpaperId: Int): Call<WallpaperItem>

    @POST("wallpapers/{id}/updatedownload")
    fun postUpdateDownload(@Path("id") wallpaperId: Int): Call<WallpaperItem>

}