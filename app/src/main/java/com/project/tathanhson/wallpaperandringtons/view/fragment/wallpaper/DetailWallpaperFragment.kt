package com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.MainActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment


class DetailWallpaperFragment : BaseFragment<FragmentDetailWallpaperBinding>(FragmentDetailWallpaperBinding::inflate) {

    lateinit var viewModel: WallpaperVM
    private lateinit var imagePath: String

    private var countFavorite : Int =0
    private var coutClick : Int = 0

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(WallpaperVM::class.java)
    }

    override fun initData() {
        CommonObject.iamgeWallperLD.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                imagePath = it.img_large
                loadPathImageToView(imagePath, binding.imgDetail)
                binding.btnFavorite.text = it.favorite.toString()
                binding.btnDownload.text = it.download.toString()
                countFavorite = it.favorite
            }
        })

    }

    override fun initView() {
        binding.btnClose.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }

        binding.imgDetail.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(mContext)
            val mBinding = BottomDialogBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(mBinding.root)
            bottomSheetDialog.show()
            listenerBottomDialog(mBinding)

        }

        binding.btnFavorite.setOnClickListener {
            if(coutClick == 0){
                countFavorite++
                coutClick++
                CommonObject.iamgeWallperLD.observe(viewLifecycleOwner, Observer { item ->
                    item?.let {
                        viewModel.postUpdateFavorite(it.id)
                        binding.btnFavorite.text = countFavorite.toString()
                        Log.d(TAG, "initData: "+it.favorite.toString())

                    }
                })
            }
        }
    }

    private fun listenerBottomDialog(mBinding: BottomDialogBinding) {
        mBinding.btnSetHomeScr.setOnClickListener {
            CommonObject.setWallpaperToScreen(this, imagePath, flagHome)
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for home screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }

        mBinding.btnSetLockScr.setOnClickListener {
            CommonObject.setWallpaperToScreen(this, imagePath, flagLock)
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for lock screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }

        mBinding.btnSetHomeAndLock.setOnClickListener {
            CommonObject.setWallpaperToScreen(this, imagePath, flagHomeAndLock)
            Toast.makeText(
                MyApplication.instance,
                "Set to home & lock screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }

        mBinding.btnSetContacts.setOnClickListener {

        }

        mBinding.btnSetDowndload.setOnClickListener {

        }

        mBinding.btnCancel.setOnClickListener {

        }
    }


    private fun loadPathImageToView(pathImage: String, imgWallpaper: ImageView) {
        Glide.with(mContext)
            .load(pathImage)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            )
            .into(imgWallpaper)
    }

    companion object {
        val TAG = DetailWallpaperFragment::class.java.name
        const val flagHome: String = "HOME"
        const val flagLock: String = "LOCK"
        const val flagHomeAndLock: String = "HOME_AND_LOCK"
        const val REQUEST_CODE = 123
    }

}