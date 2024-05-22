package com.example.wallpagerandringtons.model.repository

import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpapers
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Categories
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API: http://app2.remimobile.com/docs
 */


interface Api {

    /**
     * GET LIST WALLPAPER BY ID CATEGORY
     */
    @GET("wallpapers/category/{categoryId}/")
    fun getWallpaperList(
        @Path("categoryId") categoryId: Int,
        @Query("limit") limit: Int
    ): Call<Wallpapers>

    /**
     *  GET CATEGORY
     */
    @GET("category/")
    fun getWallpaperTitle(): Call<Categories>

    /**
     * POST UPDATE FAVORITE
     */
    @POST("wallpapers/{id}/updatefavorite")
    fun postUpdateFavorite(@Path("id") wallpaperId: Int): Call<WallpaperItem>

    /**
     *  POST UPDATE DOWNLOAD
     */

    @POST("wallpapers/{id}/updatedownload")
    fun postUpdateDownload(@Path("id") wallpaperId: Int): Call<WallpaperItem>

    /**
     * GET LIVE WALLPAPER BY ID CATEGORY LIVE WALLPAPER
     */
    @GET("wallpapers/category/{liveWallpaperId}")
    fun getLiveWallpapers(
        @Path("liveWallpaperId") categoryId: Int
    ) : Call<LiveWallpapers>

    /**
     * GET WALLPAPER BY ID
     */
    @GET("wallpapers/{id_wallpaper}")
    fun getWallpaperById(
        @Path("id_wallpaper") wallpaperId: Int
    ): Call<WallpaperItem>

    @GET("wallpapers/{id_live_wallpaper}")
    fun getLiveWallpaperById(
        @Path("id_live_wallpaper") liveWallpaperId: Int
    ): Call<LiveWallpaperItem>
}