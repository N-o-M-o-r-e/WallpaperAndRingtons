package com.project.tathanhson.wallpaperandringtons.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DownloadManager(
    private val context: Context,
    private val url: String,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {

    fun downloadWallpaper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadImage()
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                downloadImage()
            }
        }
        // Show a toast when download starts
        Toast.makeText(context, "Downloading image...", Toast.LENGTH_SHORT).show()
    }

    fun downloadImage() {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = mLoad(url)
            // Switch to the main thread to update UI
            withContext(Dispatchers.Main) {
                if (bitmap != null) {
                    mSaveMediaToStorage(bitmap)
                    // Show a toast when download completes
                    Toast.makeText(context, "Image downloaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Dummy implementation for loading and saving image
    private suspend fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        return try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
            null
        }
    }

    protected fun mStringToURL(string: String): URL? {
        return try {
            URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }
    }

    protected suspend fun mSaveMediaToStorage(bitmap: Bitmap?) {
        withContext(Dispatchers.IO) {
            val filename = "${System.currentTimeMillis()}.jpg"
            var fos: OutputStream? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }
            fos?.use {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
    }
}