package com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.MyPrefs.SharedPreferencesLiveWallpaper
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment


class DetailWallpaperFragment : BaseFragment<FragmentDetailWallpaperBinding>(FragmentDetailWallpaperBinding::inflate) {

    lateinit var viewModel: WallpaperVM
    private lateinit var imagePath: String
    private lateinit var itemWallpaper : WallpaperItem

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(WallpaperVM::class.java)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initData() {
        CommonObject.itemWallpaper.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                itemWallpaper = item
                imagePath = it.img_large
                CommonObject.loadPathImageToView(mContext, imagePath, binding.imgDetail)

                CommonObject.checkFavoriteWallpaperUI(
                    it,
                    sharedPreferencesWallpaper,
                    resources,
                    binding.btnFavorite
                )
            }
        })

    }

    @SuppressLint("LogConditional")
    override fun initView() {
        binding.btnClose.setOnClickListener {
            requireActivity().finish()
            CommonObject.itemWallpaper.value = null
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
            val id_wallpaper = itemWallpaper.id
            if (!sharedPreferencesWallpaper.isIdExist(id_wallpaper)) {
                // Update favorite to API
                viewModel.postUpdateFavorite(id_wallpaper)
                val currentWallpapers = sharedPreferencesWallpaper.getWallpapers()
                currentWallpapers.add(id_wallpaper)
                sharedPreferencesWallpaper.saveWallpapers(currentWallpapers)
                CommonObject.isFavoriteTrue(resources, binding.btnFavorite)

            } else {
                // Xóa wallpaper khỏi danh sách yêu thích
                sharedPreferencesWallpaper.removeWallpaper(id_wallpaper)
                CommonObject.isFavoriteFalse(resources, binding.btnFavorite)
                Log.e("AAAAAAAAA", "ID đã tồn tại trong SharedPreferences và đã bị xóa")
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
        val TAG = "AAAAAAAAAAAAAAAAAAAAAAAAAA"
        const val flagHome: String = "HOME"
        const val flagLock: String = "LOCK"
        const val flagHomeAndLock: String = "HOME_AND_LOCK"
        const val REQUEST_CODE = 123
    }

}