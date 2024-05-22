package com.project.tathanhson.wallpaperandringtons.view.activity.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesLiveWallpaper
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesRingtones
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesWallpaper


abstract class BaseFragment<Binding : ViewDataBinding>(private val inflate: Inflate<Binding>) :
    Fragment() {
    protected lateinit var binding: Binding
    protected lateinit var mContext: Context

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
