package com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailLiveWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper.DetailWallpaperFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM

class DetailLiveWallpaperFragment :
    BaseFragment<FragmentDetailLiveWallpaperBinding>(FragmentDetailLiveWallpaperBinding::inflate) {
    lateinit var viewModel: LiveWallpaperVM

    private var countFavorite: Int = 0

    private var isClickFavorite : Boolean = false

    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[LiveWallpaperVM::class.java]
    }

    override fun initData() {
        //Observer Item LiveWallpaper
        CommonObject.itemLiveWallpaper.observe(viewLifecycleOwner) { position ->
            position?.let {
                CommonObject.loadPathImageToView(mContext, it.img_large, binding.imgDetail)
                binding.btnFavorite.text = it.favorite.toString()
                binding.btnFavorite.text = it.favorite.toString()
                binding.btnDownload.text = it.download.toString()
                countFavorite = it.favorite
            }
        }
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
            listenerFavorites()

        }


        binding.btnShare.setOnClickListener {

        }

        binding.btnDownload.setOnClickListener {

        }
    }

    private fun listenerBottomDialog(mBinding: BottomDialogBinding, bottomSheetDialog: BottomSheetDialog) {
        mBinding.btnSetHomeScr.setOnClickListener {
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for home screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetLockScr.setOnClickListener {
            Toast.makeText(
                MyApplication.instance,
                "Set wallpaper for lock screen successfully!",
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetDialog.dismiss()
        }

        mBinding.btnSetHomeAndLock.setOnClickListener {

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

    private fun listenerFavorites() {
        if (!isClickFavorite){
            countFavorite++
            CommonObject.itemLiveWallpaper.observe(viewLifecycleOwner) { item ->
                item?.let {
                    //update favorite to API
                    viewModel.postUpdateFavorite(it.id)
                    binding.btnFavorite.text = countFavorite.toString()
                    Log.d("AAAAAAAAAAAA" ,"initData: "+it.favorite.toString())
                }
            }
            isClickFavorite = true
        }
    }

    companion object {
        val TAG = DetailLiveWallpaperFragment::class.java.name
    }

}