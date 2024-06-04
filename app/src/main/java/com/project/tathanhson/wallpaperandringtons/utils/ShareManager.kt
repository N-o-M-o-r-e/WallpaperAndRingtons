package com.project.tathanhson.wallpaperandringtons.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ShareManager(private val context: Context, private val url: String) {

    fun shareImage() {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = loadBitmapFromURL(url)
            withContext(Dispatchers.Main) {
                if (bitmap != null) {
                    val uri = saveImageToCache(bitmap)
                    uri?.let {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, it)
                            type = "image/jpeg"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share image via"))
                    }
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun loadBitmapFromURL(urlString: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val url: URL = stringToURL(urlString) ?: return@withContext null
            val connection: HttpURLConnection
            return@withContext try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun stringToURL(string: String): URL? {
        return try {
            URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }
    }

    private fun saveImageToCache(bitmap: Bitmap): Uri? {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs() // Create directory if it doesn't exist
        val file = File(cachePath, "shared_image.jpg")
        var fos: OutputStream? = null
        return try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            fos?.close()
        }
    }
}