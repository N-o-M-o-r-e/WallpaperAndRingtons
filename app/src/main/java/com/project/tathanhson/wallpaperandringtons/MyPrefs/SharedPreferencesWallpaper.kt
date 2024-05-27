package com.project.tathanhson.wallpaperandringtons.MyPrefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import org.json.JSONArray

class SharedPreferencesWallpaper {

    private val sharedPreferences: SharedPreferences = MyApplication.instance.getSharedPreferences("MyPrefsWallpapers", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveWallpapers(numbers: ArrayList<Int>) {
        val jsonArray = JSONArray()
        numbers.forEach { jsonArray.put(it) }
        editor.putString("id_wallpaper", jsonArray.toString())
        editor.apply()
    }

    fun getWallpapers(): ArrayList<Int> {
        val jsonString = sharedPreferences.getString("id_wallpaper", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<Int>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getInt(i))
        }
        Log.i("BBBBBBBBBB", "shaf wall: "+list)
        return list
    }

    fun isIdExist(id: Int): Boolean {
        val savedIds = getWallpapers()
        return savedIds.contains(id)
    }

    fun isWallpapersEmpty(): Boolean {
        val wallpaperIds = getWallpapers()
        return wallpaperIds.isEmpty()
    }


    fun removeWallpaper(id: Int) {
        val savedIds = getWallpapers()
        val updatedIds = savedIds.filter { it != id }
        saveWallpapers(ArrayList(updatedIds))

        if (isWallpapersEmpty()) {
            CommonObject._favoriteWallpapers.postValue(ArrayList())
        }
    }
}

