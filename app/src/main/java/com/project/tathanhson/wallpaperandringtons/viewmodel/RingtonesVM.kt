package com.project.tathanhson.wallpaperandringtons.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.project.tathanhson.wallpaperandringtons.model.ringtones.Ringtone
import java.io.IOException

class RingtonesVM : ViewModel() {
    val ldListFolder = MutableLiveData<ArrayList<String>>()
    val ldItemFolder = MutableLiveData<ArrayList<Ringtone>>() // Sử dụng ArrayList ở đây
    var ldItemRingtone = MutableLiveData<Ringtone>()
    fun getFoldersFromAssets(context: Context) {
        val assetManager = context.assets
        val folders = ArrayList<String>()
        try {
            val allFolders = assetManager.list("")?.toList() ?: emptyList() // Lấy tất cả các thư mục
            for (folder in allFolders) {
                try {
                    if (assetManager.list(folder)?.isNotEmpty() == true && !folder.contains(".")) {
                        folders.add(folder)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        folders.remove("webkit")
        folders.remove("images")

        Log.d("AAAAAAAAAAAAAA", folders.toString())
        ldListFolder.value = folders
    }

    fun getRingtonesFromFolder(context: Context, folderName: String) {
        val assetManager = context.assets
        val ringtones = ArrayList<Ringtone>() // Sử dụng ArrayList ở đây
        try {
            val files = assetManager.list(folderName)
            files?.let {
                for (file in it) {
                    if (file.endsWith(".mp3")) {
                        ringtones.add(Ringtone(folderName, file))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("AAAAAAAAAAAAAAAAA", "getRingtonesFromFolder: "+ringtones)
        ldItemFolder.value = ringtones
    }
}

