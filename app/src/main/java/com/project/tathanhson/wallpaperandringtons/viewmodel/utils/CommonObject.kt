package com.example.wallpagerandringtons.viewmodel.utils

import androidx.lifecycle.MutableLiveData
import com.example.wallpagerandringtons.model.WallpaperItem
import com.example.wallpagerandringtons.model.WallpaperList

object CommonObject {
    var WallpaperList: WallpaperList? = null
    var WallpaperItem: WallpaperItem? = null

    var iamgeWallperLD = MutableLiveData<WallpaperItem>()
    var listWallperLD = MutableLiveData<WallpaperList>()

    var positionItem = MutableLiveData<Int>()
}