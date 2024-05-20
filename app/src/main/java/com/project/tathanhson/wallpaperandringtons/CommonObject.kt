package com.project.tathanhson.wallpaperandringtons

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment

object CommonObject {

    //livedata Wallpaper
    var iamgeWallperLD = MutableLiveData<WallpaperItem>()
    var positionItemWallpaper = MutableLiveData<Int>()

    //livedata Ringtone
    var listCategorysRingtone = MutableLiveData<ArrayList<String>>()  // list category
    var categoryRingtone = MutableLiveData<String>() // category in list

    var listDataRingtone = MutableLiveData<ArrayList<Data>>() // listData
    var positionDataRingtone = MutableLiveData<Int>()  // position item in listData



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