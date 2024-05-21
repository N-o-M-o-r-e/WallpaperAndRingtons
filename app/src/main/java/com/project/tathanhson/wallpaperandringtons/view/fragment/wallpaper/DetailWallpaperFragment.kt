package com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment


class DetailWallpaperFragment : BaseFragment<FragmentDetailWallpaperBinding>(FragmentDetailWallpaperBinding::inflate) {

    lateinit var viewModel: WallpaperVM
    private lateinit var imagePath: String

    private var countFavorite : Int =0
    private var isClickFavotite: Boolean = false

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(WallpaperVM::class.java)
    }

    override fun initData() {
        CommonObject.itemWallpaper.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                imagePath = it.img_large
                CommonObject.loadPathImageToView(mContext, imagePath, binding.imgDetail)
                binding.btnFavorite.text = it.favorite.toString()
                binding.btnDownload.text = it.download.toString()
                countFavorite = it.favorite
            }
        })

    }

    override fun initView() {
        binding.btnClose.setOnClickListener {
            requireActivity().finish()
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
            if (!isClickFavotite) {
                countFavorite++
                CommonObject.itemWallpaper.observe(viewLifecycleOwner, Observer { item ->
                    item?.let {
                        //update favorite to API
                        viewModel.postUpdateFavorite(it.id)
                        binding.btnFavorite.text = countFavorite.toString()
                        Log.d(TAG, "initData: "+it.favorite.toString())
                    }
                })
                isClickFavotite = true
            }

        }
    }

    private fun listenerBottomDialog(
        mBinding: BottomDialogBinding,
        bottomSheetDialog: BottomSheetDialog
    ) {
        mBinding.btnSetHomeScr.setOnClickListener {
            CommonObject.setWallpaperToScreen(this, imagePath, flagHome)
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for home screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetLockScr.setOnClickListener {
            CommonObject.setWallpaperToScreen(this, imagePath, flagLock)
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for lock screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetHomeAndLock.setOnClickListener {
            CommonObject.setWallpaperToScreen(this, imagePath, flagHomeAndLock)
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


    companion object {
        val TAG = DetailWallpaperFragment::class.java.name
        const val flagHome: String = "HOME"
        const val flagLock: String = "LOCK"
        const val flagHomeAndLock: String = "HOME_AND_LOCK"
        const val REQUEST_CODE = 123
    }

}