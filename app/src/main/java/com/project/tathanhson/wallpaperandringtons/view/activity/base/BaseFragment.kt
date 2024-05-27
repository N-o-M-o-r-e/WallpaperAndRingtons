package com.project.tathanhson.wallpaperandringtons.view.activity.base

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesLiveWallpaper
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesRingtones
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesWallpaper
import kotlinx.coroutines.Dispatchers
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


abstract class BaseFragment<Binding : ViewDataBinding>(private val inflate: Inflate<Binding>) :
    Fragment() {
    protected lateinit var binding: Binding
    protected lateinit var mContext: Context
    protected lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    protected val sharedPreferencesWallpaper = SharedPreferencesWallpaper()
    protected val sharedPreferencesRingtones = SharedPreferencesRingtones()
    protected val sharedPreferencesLiveWallpaper = SharedPreferencesLiveWallpaper()

    protected abstract fun initViewModel()

    protected abstract fun initData()

    protected abstract fun initView()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        initData()
    }

}
