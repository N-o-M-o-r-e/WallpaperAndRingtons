package com.project.tathanhson.wallpaperandringtons

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpapers
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Categories
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.CategoryWallpaper
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment

object CommonObject {

    //livedata Wallpaper
    var categoryWallpaper = MutableLiveData<CategoryWallpaper?>() // category title
    var listCategoryWallpaper = MutableLiveData<Categories?>()   // list category

    var itemWallpaper = MutableLiveData<WallpaperItem?>()       //item wallpaper
    var listWallpaper = MutableLiveData<Wallpapers?>()       //list item wallpaper


    var positionItemWallpaper = MutableLiveData<Int>()          // item selected in recyclerview list item wallpaper
    var countFavoriteWallpaper = MutableLiveData<Int>()         // count favorite

    //livedata Ringtone
    var listCategorysRingtone = MutableLiveData<ArrayList<String>>()  // list category
    var categoryRingtone = MutableLiveData<String>()                  // category in list

    var listDataRingtone = MutableLiveData<ArrayList<Data>>()   // listData
    var positionDataRingtone = MutableLiveData<Int>()           // position item in listData

    //livedata livewallpaper
    var listLiveWallpapers = MutableLiveData<LiveWallpapers>()      //list Live Wallpaper
    var positionLiveWallpaper = MutableLiveData<Int>()              // position item in LiveWallpapers
    var itemLiveWallpaper = MutableLiveData<LiveWallpaperItem>()    // position item in LiveWallpapers


    fun loadPathImageToView(context: Context, pathImage: String, imgWallpaper: ImageView) {
        Glide.with(context)
            .load(pathImage)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.img_loadimage)
                    .error(R.drawable.img_loadimage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            )
            .into(imgWallpaper)
    }

    fun setWallpaperToScreen(view: DetailWallpaperFragment, imagePath: String, flagSystem: String) {
        Glide.with(view)
            .asBitmap()
            .load(imagePath)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    val wallpaperManager = WallpaperManager.getInstance(MyApplication.instance)
                    try {
                        when (flagSystem) {
                            "HOME" -> wallpaperManager.setBitmap(
                                resource,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )

                            "LOCK" -> wallpaperManager.setBitmap(
                                resource,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )

                            "HOME_AND_LOCK" -> wallpaperManager.setBitmap(resource)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            MyApplication.instance,
                            "Load image failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

}