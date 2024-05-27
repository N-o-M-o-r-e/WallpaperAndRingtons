package com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailLiveWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.service.VideoLiveWallpaperService
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DetailLiveWallpaperFragment :
    BaseFragment<FragmentDetailLiveWallpaperBinding>(FragmentDetailLiveWallpaperBinding::inflate) {
    lateinit var viewModel: LiveWallpaperVM

    private var countFavorite: Int = 0

    private lateinit var item : LiveWallpaperItem

    private lateinit var url : String





    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[LiveWallpaperVM::class.java]
    }

    @SuppressLint("LogConditional")
    override fun initData() {
        //Observer Item LiveWallpaper
        CommonObject.itemLiveWallpaper.observe(viewLifecycleOwner) { itemLiveWallpaper ->
            itemLiveWallpaper?.let {
                item = itemLiveWallpaper
                Log.d("AAAAAAAAAAAAA", "item: img_larger "+item.img_large+" \n img_thumb"+item.img_thumb)
                CommonObject.URL_LIVE_WALLPAPER = itemLiveWallpaper.img_large
                Log.d("AAAAAAAAAAAAA", "initData: +url= "+ CommonObject.URL_LIVE_WALLPAPER)
                url = item.img_large
                binding.imgDetail.setVideoPath(url)
                binding.imgDetail.start()
                binding.imgDetail.setOnCompletionListener { mediaPlayer ->
                    mediaPlayer?.let {
                        it.seekTo(0)
                        it.start()
                    }
                }

                countFavorite = item.favorite
                CommonObject.checkFavoriteLiveWallpaperUI(
                    item,
                    sharedPreferencesLiveWallpaper,
                    resources,
                    binding.btnFavorite
                )
            }
        }
    }

    override fun initView() {
        binding.btnClose.setOnClickListener {
            requireActivity().finish()
            CommonObject.itemLiveWallpaper.value = null
        }

        binding.imgDetail.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(mContext)
            val mBinding = BottomDialogBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(mBinding.root)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            bottomSheetDialog.show()
            listenerBottomDialog(mBinding, bottomSheetDialog)

        }

        binding.btnFavorite.setOnClickListener {
            val id_live_wallpaper = item.id
            if (!sharedPreferencesLiveWallpaper.isIdExist(id_live_wallpaper)){
                //update favorite to API
                viewModel.postUpdateFavorite(item.id)
                val currentWallpapers = sharedPreferencesLiveWallpaper.getWallpapers()
                currentWallpapers.add(id_live_wallpaper)
                sharedPreferencesLiveWallpaper.saveWallpapers(currentWallpapers)
                CommonObject.isFavoriteTrue(resources, binding.btnFavorite)
            }else{
                sharedPreferencesLiveWallpaper.removeWallpaper(id_live_wallpaper)
                CommonObject.isFavoriteFalse(resources, binding.btnFavorite)
                Toast.makeText(mContext, "Live Wallpaper removed from favorites!", Toast.LENGTH_SHORT).show()
            }

        }


        binding.btnShare.setOnClickListener {
            //share file
            shareVideo()
        }

        binding.btnDownload.setOnClickListener {
            Toast.makeText(mContext, "Downloading...", Toast.LENGTH_SHORT).show()
            checkPermissionsAndDownloadVideo()
        }
    }

    private fun shareVideo() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "video/mp4"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(url))
        }
        startActivity(Intent.createChooser(shareIntent, "Share video using"))
    }




    private fun checkPermissionsAndDownloadVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 và cao hơn
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
                    REQUEST_READ_MEDIA_VIDEO
                )
            } else {
                downloadVideo()
            }
        } else { // Android 9 và thấp hơn
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE
                )
            } else {
                downloadVideo()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_STORAGE, REQUEST_READ_MEDIA_VIDEO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadVideo()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun downloadVideo() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.connect()

                val inputStream: InputStream = urlConnection.inputStream
                val filename = "live_wallpaper_${System.currentTimeMillis()}.mp4"

                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, filename)
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Download complete", Toast.LENGTH_SHORT).show()
                    // Scan the file so it appears in the gallery
                    MediaScannerConnection.scanFile(
                        requireContext(),
                        arrayOf(file.absolutePath),
                        arrayOf("video/mp4")
                    ) { path, uri ->
                        Log.e(TAG, "File scanned into media library: $path")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun listenerBottomDialog(mBinding: BottomDialogBinding, bottomSheetDialog: BottomSheetDialog) {
        mBinding.btnSetHomeScr.setOnClickListener {
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for home screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(mContext, VideoLiveWallpaperService::class.java))
            }
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetLockScr.setOnClickListener {
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for lock screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetHomeAndLock.setOnClickListener {

            Toast.makeText(
                MyApplication.instance,
                "Set to home & lock screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetContacts.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetDowndload.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        mBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    private fun listenerFavorites() {

    }


    companion object {
        const val TAG = "AAAAAAAAAAAAAAAAAAAA"
        private const val REQUEST_WRITE_STORAGE = 115
        private const val REQUEST_READ_MEDIA_VIDEO = 116
    }

}