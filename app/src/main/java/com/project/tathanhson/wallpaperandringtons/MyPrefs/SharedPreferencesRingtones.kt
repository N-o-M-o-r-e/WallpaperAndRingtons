package com.project.tathanhson.wallpaperandringtons.MyPrefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.mediaplayer.model.Ringtones
import com.project.tathanhson.wallpaperandringtons.MyApplication
import org.json.JSONArray

class SharedPreferencesRingtones {

    private val sharedPreferences: SharedPreferences = MyApplication.instance.getSharedPreferences("MyPrefsRingtones", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val gson = Gson()

    // Lưu danh sách Data vào SharedPreferences
    fun saveRingtones(ringtones: ArrayList<Data>) {
        val jsonArray = JSONArray()
        ringtones.forEach { jsonArray.put(gson.toJson(it)) }
        editor.putString("ringtones", jsonArray.toString())
        editor.apply()
    }

    // Lấy danh sách Data từ SharedPreferences
    fun getRingtones(): ArrayList<Data> {
        val jsonString = sharedPreferences.getString("ringtones", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<Data>()
        for (i in 0 until jsonArray.length()) {
            val ringtoneItem = gson.fromJson(jsonArray.getString(i), Data::class.java)
            list.add(ringtoneItem)
        }
        return list
    }

    // Kiểm tra xem một Ringtone đã tồn tại trong danh sách hay chưa
    fun isRingtoneExist(link: String): Boolean {
        val savedRingtones = getRingtones()
        return savedRingtones.any { it.link == link }
    }

    // Xóa một Ringtone khỏi danh sách và lưu lại
    fun removeRingtone(link: String) {
        val savedRingtones = getRingtones()
        val updatedRingtones = savedRingtones.filter { it.link != link }
        saveRingtones(ArrayList(updatedRingtones))
    }
}
