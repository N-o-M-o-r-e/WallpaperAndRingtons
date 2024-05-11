package com.example.wallpagerandringtons.view.fragment.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallpagerandringtons.model.WallpaperList
import com.example.wallpagerandringtons.view.adapter.wallpapers.DesWallpaperAdapter
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailWallpaperBinding
import kotlin.math.log


class DetailWallpaperFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentDetailWallpaperBinding
    private lateinit var viewModel: WallpaperMV
    private var adapter: DesWallpaperAdapter ?= null
    private var listWallpaper: WallpaperList? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailWallpaperBinding.inflate(inflater, container, false)
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(WallpaperMV::class.java)
        viewModel.getAPI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiView()
    }

    private fun intiView() {
        // Trong Activity hoặc Fragment:
        viewModel.ldListWallpaper.observe(viewLifecycleOwner, Observer { wallpaperList ->
            wallpaperList?.let {
                listWallpaper = wallpaperList

                adapter = DesWallpaperAdapter(context, viewModel, viewLifecycleOwner, listWallpaper!!)
                binding.rcvWallpaper.layoutManager = LinearLayoutManager(getContext())
                binding.rcvWallpaper.adapter = adapter
            }
        })


        var itemSelect = CommonObject.WallpaperItem




    }
}