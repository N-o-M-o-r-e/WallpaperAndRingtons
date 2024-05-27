package com.project.tathanhson.wallpaperandringtons.MyPrefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.MyApplication
import org.json.JSONArray

class SharedPreferencesRingtones {

    private val sharedPreferences: SharedPreferences = MyApplication.instance.getSharedPreferences("MyPrefsRingtones", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val gson = Gson()

    fun saveRingtones(ringtones: ArrayList<Data>) {
        val jsonArray = JSONArray()
        ringtones.forEach { jsonArray.put(gson.toJson(it)) }
        editor.putString("ringtones", jsonArray.toString())
        editor.apply()
    }

    fun removeRingtone(link: String) {
        val savedRingtones = getRingtones()
        val updatedRingtones = savedRingtones.filter { it.link != link }
        saveRingtones(ArrayList(updatedRingtones))
    }

    fun isRingtoneExist(link: String): Boolean {
        val savedRingtones = getRingtones()
        return savedRingtones.any { it.link == link }
    }


    fun getRingtones(): ArrayList<Data> {
        val jsonString = sharedPreferences.getString("ringtones", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<Data>()
        for (i in 0 until jsonArray.length()) {
            val ringtoneItem = gson.fromJson(jsonArray.getString(i), Data::class.java)
            list.add(ringtoneItem)
        }
        Log.i("BBBBBBBB", "shaf ring: " + list)
        return list
    }


}
