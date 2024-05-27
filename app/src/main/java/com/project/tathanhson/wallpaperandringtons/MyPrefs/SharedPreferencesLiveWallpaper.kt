package com.project.tathanhson.wallpaperandringtons.MyPrefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import org.json.JSONArray

class SharedPreferencesLiveWallpaper {
    private val sharedPreferences: SharedPreferences = MyApplication.instance.getSharedPreferences("MyPrefsLiveWallpaper", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveWallpapers(id_livewallpaper: ArrayList<Int>) {
        val jsonArray = JSONArray()
        id_livewallpaper.forEach { jsonArray.put(it) }
        editor.putString("id_live_wallpaper", jsonArray.toString())
        editor.apply()
    }

    fun getWallpapers(): ArrayList<Int> {
        val jsonString = sharedPreferences.getString("id_live_wallpaper", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<Int>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getInt(i))
        }
        Log.i("BBBBBBBB", "Shaf live wall: " + list)
        return list
    }

    fun isIdExist(id: Int): Boolean {
        val savedIds = getWallpapers()
        return savedIds.contains(id)
    }

    fun isWallpapersEmpty(): Boolean {
        val liveWallpaperIds = getWallpapers()
        return liveWallpaperIds.isEmpty()
    }


    fun removeWallpaper(id: Int) {
        val savedIds = getWallpapers()
        val updatedIds = savedIds.filter { it != id }
        saveWallpapers(ArrayList(updatedIds))
        if (isWallpapersEmpty()) {
            CommonObject._favoriteLiveWallpapers.postValue(ArrayList())
        }
    }
}