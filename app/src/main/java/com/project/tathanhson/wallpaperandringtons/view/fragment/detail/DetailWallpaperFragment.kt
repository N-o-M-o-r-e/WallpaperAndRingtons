package com.project.tathanhson.wallpaperandringtons.view.fragment.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.HomeActivity

class DetailWallpaperFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentDetailWallpaperBinding
    lateinit var viewModel: WallpaperMV

    companion object {
        val TAG = DetailWallpaperFragment::class.java.name
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(WallpaperMV::class.java)
        binding = FragmentDetailWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        CommonObject.iamgeWallperLD.observe(viewLifecycleOwner, Observer { item ->
            item?.let { loadPathImageToView(it.img_thumb, binding.imgDetail) }
        })

        binding.btnClose.setOnClickListener {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }


    private fun loadPathImageToView(pathImage: String, imgWallpaper: ImageView) {
        Glide.with(context)
            .load(pathImage)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            )
            .into(imgWallpaper)
    }

}