package com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpagerandringtons.view.adapter.wallpapers.ListWallpaperAdapter
import com.example.wallpagerandringtons.view.adapter.wallpapers.TitleWallpaperAdapter
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment


class WallpaperFragment :
    BaseFragment<FragmentWallpaperBinding>(FragmentWallpaperBinding::inflate) {

    lateinit var viewModel: WallpaperVM
    private var adapter: ListWallpaperAdapter? = null
    private var adapterTitle: TitleWallpaperAdapter? = null
    private var listWallpaper: Wallpapers? = null

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[WallpaperVM::class.java]
    }

    override fun initData() {
        //call api để lấy dữ liệu title category
        viewModel.getApiCategoryWallpaper()

        //sau đó call api với id của title trả về tương ứng
        CommonObject.categoryWallpaper.observe(viewLifecycleOwner, Observer { titleSelected ->
            titleSelected?.id.let {
                viewModel.getApiWallpaperDetail(titleSelected!!.id)
            }
        })
    }

    override fun initView() {
        //Quan sát dữ liệu khi call title category trả về để gán lên adapter
        CommonObject.listCategoryWallpaper.observe(viewLifecycleOwner, Observer { listTitle ->
            listTitle?.let {
                adapterTitle = TitleWallpaperAdapter(mContext, viewModel, viewLifecycleOwner, listTitle)
                binding.rcvTitle.adapter = adapterTitle
            }
        })

        //List Wallpaper
        CommonObject.listWallpaper.observe(viewLifecycleOwner, Observer { wallpaperList ->
            wallpaperList?.let {
                listWallpaper = wallpaperList
                adapter = ListWallpaperAdapter(mContext, viewModel, viewLifecycleOwner, wallpaperList)
                val layoutManager = GridLayoutManager(mContext, 3)
                binding.rcvWallpaper.layoutManager = layoutManager
                binding.rcvWallpaper.adapter = adapter
            }
        })

    }

}