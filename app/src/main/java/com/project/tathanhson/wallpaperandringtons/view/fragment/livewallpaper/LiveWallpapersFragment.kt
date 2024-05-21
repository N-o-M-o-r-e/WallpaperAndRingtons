package com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentLiveWallpapersBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.view.adapter.live_wallpaper.LiveWallpaperAdapter
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM


class LiveWallpapersFragment :
    BaseFragment<FragmentLiveWallpapersBinding>(FragmentLiveWallpapersBinding::inflate) {

    lateinit var viewModel: LiveWallpaperVM
    private var adapter: LiveWallpaperAdapter? = null
    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[LiveWallpaperVM::class.java]
    }

    override fun initData() {
        viewModel.getApiLiveWallpaper(29)
        /*
         * hiện tại 29 là id_category của live_wallpaper
         */

    }

    override fun initView() {
        CommonObject.listLiveWallpapers.observe(viewLifecycleOwner) { liveWallpapers ->
            liveWallpapers?.let {
                adapter = LiveWallpaperAdapter(mContext, viewModel, viewLifecycleOwner, it)
                val layoutManager = GridLayoutManager(mContext, 3)
                binding.crvLiveWallpaper.layoutManager = layoutManager
                binding.crvLiveWallpaper.adapter = adapter
            }
        }
    }
}