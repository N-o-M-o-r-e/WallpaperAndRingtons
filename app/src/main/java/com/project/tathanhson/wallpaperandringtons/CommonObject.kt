package com.project.tathanhson.wallpaperandringtons

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesLiveWallpaper
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesRingtones
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesWallpaper
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

    //livedata Ringtones
    var listCategorysRingtones = MutableLiveData<ArrayList<String>>()  // list category
    var categoryRingtone = MutableLiveData<String>()                  // category in list

    var listDataRingtone = MutableLiveData<ArrayList<Data>>()   // listData
    var positionDataRingtone = MutableLiveData<Int>()           // position item in listData

    //livedata livewallpaper
    var listLiveWallpapers = MutableLiveData<LiveWallpapers>()      //list Live Wallpaper
    var positionLiveWallpaper = MutableLiveData<Int>()              // position item in LiveWallpapers
    var itemLiveWallpaper = MutableLiveData<LiveWallpaperItem>()    // position item in LiveWallpapers

    //favorite

    val _favoriteWallpapers = MutableLiveData<ArrayList<WallpaperItem>>()
    val favoriteWallpapers: LiveData<ArrayList<WallpaperItem>> get() = _favoriteWallpapers

    val _favoriteRingtones = MutableLiveData<ArrayList<Data>>()
    val favoriteRingtones: LiveData<ArrayList<Data>> get() = _favoriteRingtones

    val _favoriteLiveWallpapers = MutableLiveData<ArrayList<LiveWallpaperItem>>()
    val favoriteLiveWallpapers: LiveData<ArrayList<LiveWallpaperItem>> get() = _favoriteLiveWallpapers


    fun loadPathImageToView(context: Context, pathImage: String, imgWallpaper: ImageView) {
        if (pathImage == null){

        }

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

    fun checkFavoriteWallpaperUI(
        wallpaperItem: WallpaperItem,
        sharedPreferencesWallpaper: SharedPreferencesWallpaper,
        resources: Resources,
        viewFavorite: TextView
    ) {
        if (sharedPreferencesWallpaper.isIdExist(wallpaperItem.id)) {
            isFavoriteTrue(resources, viewFavorite)
        }else{
            isFavoriteFalse(resources, viewFavorite)
        }
    }

    fun checkFavoriteRingtoneUI(
        dataRingtone: Data,
        sharedPreferencesRingtones: SharedPreferencesRingtones,
        resources: Resources,
        viewFavorite: TextView
    ) {
        if (sharedPreferencesRingtones.isRingtoneExist(dataRingtone.link)) {
            isFavoriteTrue(resources, viewFavorite)
        }else{
            isFavoriteFalse(resources, viewFavorite)
        }
    }

    fun checkFavoriteLiveWallpaperUI(
        wallpaperItem: LiveWallpaperItem,
        sharedPreferencesLiveWallpaper: SharedPreferencesLiveWallpaper,
        resources: Resources,
        viewFavorite: TextView
    ) {
        if (sharedPreferencesLiveWallpaper.isIdExist(wallpaperItem.id)) {
            isFavoriteTrue(resources, viewFavorite)
        }else{
            isFavoriteFalse(resources, viewFavorite)
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun isFavoriteTrue(resources: Resources, viewFavorite: TextView){
        val drawable = resources.getDrawable(R.drawable.ic_favorite_on)
        viewFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun isFavoriteFalse(resources: Resources, viewFavorite: TextView){
        val drawable = resources.getDrawable(R.drawable.ic_favorite_off)
        viewFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null)
    }

}