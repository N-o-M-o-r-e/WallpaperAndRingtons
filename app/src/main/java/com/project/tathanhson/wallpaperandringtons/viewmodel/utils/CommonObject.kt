package com.example.wallpagerandringtons.viewmodel.utils

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.view.fragment.detail.DetailWallpaperFragment

object CommonObject {
    var WallpaperList: WallpaperList? = null
    var WallpaperItem: WallpaperItem? = null

    var iamgeWallperLD = MutableLiveData<WallpaperItem>()
    var listWallperLD = MutableLiveData<WallpaperList>()

    var positionItem = MutableLiveData<Int>()

    var imageFile = ""

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