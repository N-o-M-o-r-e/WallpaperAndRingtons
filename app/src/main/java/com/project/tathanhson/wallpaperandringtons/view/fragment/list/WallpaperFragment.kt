package com.example.wallpagerandringtons.view.fragment.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
import com.example.wallpagerandringtons.view.adapter.wallpapers.ListWallpaperAdapter
import com.example.wallpagerandringtons.view.adapter.wallpapers.TitleWallpaperAdapter
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Title


class WallpaperFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentWallpaperBinding

    lateinit var viewModel: WallpaperMV
    private var adapter: ListWallpaperAdapter? = null
    private var adapterTitle: TitleWallpaperAdapter? = null
    private var listWallpaper: WallpaperList? = null


    var listTitle: ArrayList<Title> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWallpaperBinding.inflate(inflater, container, false)
        initViewModel()
        crateData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[WallpaperMV::class.java]
        intiView()
    }

    private fun crateData() {

        viewModel.ldListTitle.observe(viewLifecycleOwner, Observer{ listTitle ->
            listTitle?.let {
                adapterTitle = TitleWallpaperAdapter(context, viewModel, viewLifecycleOwner, listTitle)
                binding.rcvTitle.adapter = adapterTitle
            }

        })



    }

    private fun initViewModel() {
        viewModel.getApiWallpaperTitle()

        viewModel.ldItemTitle.observe(viewLifecycleOwner, Observer{ titleSelected ->
            titleSelected?.id.let {
                viewModel.getApiWallpaperDetail(titleSelected!!.id)
            }
        })

    }


    private fun intiView() {
        viewModel.ldListWallpaper.observe(viewLifecycleOwner, Observer { wallpaperList ->
            wallpaperList?.let {
                listWallpaper = wallpaperList
                adapter =
                    ListWallpaperAdapter(context, viewModel, viewLifecycleOwner, wallpaperList)
                val layoutManager = GridLayoutManager(context, 3)
                binding.rcvWallpaper.layoutManager = layoutManager
                binding.rcvWallpaper.adapter = adapter
            }
        })
    }
}