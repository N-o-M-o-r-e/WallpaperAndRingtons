package com.project.tathanhson.wallpaperandringtons.viewmodel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.mediaplayer.model.Ringtone
import org.json.JSONArray

class RingtonesVM : ViewModel() {
    private val ringtones: ArrayList<Ringtone> = ArrayList()
    private var isDataInitialized = false
    var positionDataRingtone = MutableLiveData<Int>()

    fun readJSONToMediaPlayerList(resources: Resources, resourceId: Int): ArrayList<Ringtone> {
        if (!isDataInitialized) {
            val jsonArray = readJSONFromResource(resources, resourceId)
            for (i in 0 until jsonArray.length()) {
                val categoryObject = jsonArray.getJSONObject(i)
                val category = categoryObject.getString("category")
                val dataArray = categoryObject.getJSONArray("data")
                val dataList = ArrayList<Data>()
                for (j in 0 until dataArray.length()) {
                    val dataObject = dataArray.getJSONObject(j)
                    val name = dataObject.getString("name")
                    val link = dataObject.getString("link")
                    val size = dataObject.getString("size")
                    val time = dataObject.getString("time")
                    dataList.add(Data(link, name, size, time))
                }
                ringtones.add(Ringtone(category, dataList))
            }
            CommonObject.listCategorysRingtone.value = getCategoryList()
            isDataInitialized = true
        }
        return ringtones
    }

    private fun readJSONFromResource(resources: Resources, resourceId: Int): JSONArray {
        val inputStream = resources.openRawResource(resourceId)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return JSONArray(jsonString)
    }

    fun getCategoryList(): ArrayList<String> {
        val categories = ArrayList<String>()
        for (ringtone in ringtones) {
            categories.add(ringtone.category)
        }
        return categories
    }

    fun getDataListForCategory(category: String): ArrayList<Data>? {
        for (ringtone in ringtones) {
            if (ringtone.category == category) {
                Log.i("AAAAAAAAAAAA", "DataList for category $category: ${ringtone.data}")
                CommonObject.listDataRingtone.value = ringtone.data
                return ringtone.data
            }
        }
        return null
    }
}


