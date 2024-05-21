package com.example.wallpagerandringtons.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallpagerandringtons.model.repository.Api
import com.example.wallpagerandringtons.model.repository.RetrofitHelper
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Categories
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperVM : ViewModel() {
    var itemWallpaper = MutableLiveData<WallpaperItem?>()

    // Get API Category
    fun getApiCategoryWallpaper() {
        val listTitle = RetrofitHelper.getInstance().create(Api::class.java)
        listTitle.getWallpaperTitle().enqueue(object : Callback<Categories> {
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                val listTitleWallpaper = response.body()
                listTitleWallpaper?.let {

                    listTitleWallpaper.let { list ->
                        val indexOfId13 = list.indexOfFirst { it.id == 13 }
                        if (indexOfId13 != -1) {
                            val title13 = list.removeAt(indexOfId13)
                            list.add(0, title13)
                        }
                    }

                    //remove id title =29
                    listTitleWallpaper.let { list ->
                        val iterator = list.iterator()
                        while (iterator.hasNext()) {
                            val title = iterator.next()
                            if (title.id == 29) {
                                iterator.remove()
                                break
                            }
                        }
                    }
                    Log.d("AAAAAAAAAAAAAA", "API Wallapper: "+listTitleWallpaper)
                    CommonObject.listCategoryWallpaper.value = listTitleWallpaper
                    CommonObject.categoryWallpaper.value = listTitleWallpaper[0]
                }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {

            }
        })
    }

    //Get detail from CategoryID
    fun getApiWallpaperDetail(categoryId: Int) {
        val listWallpaper = RetrofitHelper.getInstance().create(Api::class.java)

        listWallpaper.getWallpaperList(categoryId,100).enqueue(object : Callback<Wallpapers> {

            override fun onResponse(call: Call<Wallpapers>, response: Response<Wallpapers>) {
                val listWallpaper = response.body()
                listWallpaper?.let {
                    CommonObject.listWallpaper.value = listWallpaper
//                    Log.e("AAAAAAAAAAAAAAAAAAAAA", "onResponse: "+listWallpaper, )
                }
            }

            override fun onFailure(call: Call<Wallpapers>, t: Throwable) {
//                Log.e("AAAAAAAAAAAAAAAAAAAAA", "onFailure: " + t.message)
            }
        })
    }



    fun postUpdateFavorite(wallpaperId: Int) {
        val favoriteWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        favoriteWallpaper.postUpdateFavorite(wallpaperId).enqueue(object : Callback<WallpaperItem> {
            override fun onResponse(call: Call<WallpaperItem>, response: Response<WallpaperItem>) {
//                Log.d("AAAAAAAAAAAAA", "onResponse: Update success: " + response.body())
                response.body()
            }
            override fun onFailure(call: Call<WallpaperItem>, t: Throwable) {
//                Log.d("AAAAAAAAAAAAA", "onFailure: " + t.message)
            }
        })

    }

    fun postUpdateDownload(wallpaperId: Int) {
        val downloadsWallpaper = RetrofitHelper.getInstance().create(Api::class.java)
        downloadsWallpaper.postUpdateFavorite(wallpaperId)
            .enqueue(object : Callback<WallpaperItem> {
                override fun onResponse(
                    call: Call<WallpaperItem>,
                    response: Response<WallpaperItem>
                ) {
//                    Log.d("AAAAAAAAAAAAA", "onResponse: download success: " + response.body())
                    response.body()
                }

                override fun onFailure(call: Call<WallpaperItem>, t: Throwable) {
//                    Log.d("AAAAAAAAAAAAA", "onFailure: " + t.message)
                }

            })

    }

}



