package com.project.tathanhson.wallpaperandringtons.MyPrefs

import android.content.Context
import android.content.SharedPreferences
import com.project.tathanhson.wallpaperandringtons.MyApplication
import org.json.JSONArray

class SharedPreferencesWallpaper {

    private val sharedPreferences: SharedPreferences = MyApplication.instance.getSharedPreferences("MyPrefsWallpapers", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Lưu danh sách các id_wallpaper vào SharedPreferences
    fun saveWallpapers(numbers: ArrayList<Int>) {
        val jsonArray = JSONArray()
        numbers.forEach { jsonArray.put(it) }
        editor.putString("id_wallpaper", jsonArray.toString())
        editor.apply()
    }

    // Lấy danh sách các id_wallpaper từ SharedPreferences
    fun getWallpapers(): ArrayList<Int> {
        val jsonString = sharedPreferences.getString("id_wallpaper", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<Int>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getInt(i))
        }
        return list
    }

    // Kiểm tra xem một ID đã tồn tại trong danh sách hay chưa
    fun isIdExist(id: Int): Boolean {
        val savedIds = getWallpapers()
        return savedIds.contains(id)
    }
}

