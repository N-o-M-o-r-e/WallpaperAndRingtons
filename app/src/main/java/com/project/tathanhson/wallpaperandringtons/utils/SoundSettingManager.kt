package com.project.tathanhson.wallpaperandringtons.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SoundSettingManager(
    private val context: AppCompatActivity,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {

    private val TAG = "SoundSettingManager"

    fun setAlarmSound(url: String, ringtoneType: Int) {
        if (Settings.System.canWrite(context)) {
            handlePermissionAndDownload(url, ringtoneType)
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivityForResult(intent, 200)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, url: String, ringtoneType: Int) {
        if (requestCode == 200 && Settings.System.canWrite(context)) {
            handlePermissionAndDownload(url, ringtoneType)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePermissionAndDownload(url: String, ringtoneType: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadAndSetAlarmSound(url, ringtoneType)
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                downloadAndSetAlarmSound(url, ringtoneType)
            }
        }
    }

    private fun downloadAndSetAlarmSound(url: String, ringtoneType: Int) {
        context.lifecycleScope.launch {
            try {
                Toast.makeText(context, "Downloading file...", Toast.LENGTH_SHORT).show()
                val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES)
                if (downloadDir?.exists() == false) {
                    downloadDir.mkdirs()
                }
                val audioFile = File(downloadDir, "SoundRingtone.mp3")
                downloadFile(url, audioFile)
                setDefaultAlarmSound(audioFile, ringtoneType)
                Log.d(TAG, "Alarm sound set successfully: ${audioFile.absolutePath}")
                Toast.makeText(context, "Alarm sound set successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to download and set alarm sound", e)
                Toast.makeText(context, "Failed to set alarm sound", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun downloadFile(url: String, outputFile: File) {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Failed to download file: $response")

                response.body?.byteStream()?.use { input ->
                    FileOutputStream(outputFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    private suspend fun setDefaultAlarmSound(audioFile: File, ringtoneType: Int) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Setting default alarm sound")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", audioFile)
            Log.d(TAG, "Ringtone URI: $uri")
            RingtoneManager.setActualDefaultRingtoneUri(context, ringtoneType, uri)
            Log.d(TAG, "Alarm sound path: ${audioFile.absolutePath}")
        }
    }
}
