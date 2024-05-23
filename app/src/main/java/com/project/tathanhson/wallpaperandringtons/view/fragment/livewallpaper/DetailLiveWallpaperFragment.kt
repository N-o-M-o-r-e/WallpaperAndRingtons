package com.project.tathanhson.wallpaperandringtons.view.fragment.livewallpaper

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailLiveWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM

class DetailLiveWallpaperFragment :
    BaseFragment<FragmentDetailLiveWallpaperBinding>(FragmentDetailLiveWallpaperBinding::inflate) {
    lateinit var viewModel: LiveWallpaperVM

    private var countFavorite: Int = 0

    private var isClickFavorite : Boolean = false

    private lateinit var item : LiveWallpaperItem

    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[LiveWallpaperVM::class.java]
    }

    override fun initData() {
        //Observer Item LiveWallpaper
        CommonObject.itemLiveWallpaper.observe(viewLifecycleOwner) { itemLiveWallpaper ->
            itemLiveWallpaper?.let {
                item = itemLiveWallpaper
                CommonObject.loadPathImageToView(mContext, item.img_large, binding.imgDetail)
                countFavorite = item.favorite
                CommonObject.checkFavoriteLiveWallpaperUI(
                    item,
                    sharedPreferencesLiveWallpaper,
                    resources,
                    binding.btnFavorite
                )
            }
        }
    }

    override fun initView() {
        binding.btnClose.setOnClickListener {
            requireActivity().finish()
            CommonObject.itemLiveWallpaper.value = null
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
            val id_live_wallpaper = item.id
            if (!sharedPreferencesLiveWallpaper.isIdExist(id_live_wallpaper)){
                //update favorite to API
                viewModel.postUpdateFavorite(item.id)

                val currentWallpapers = sharedPreferencesLiveWallpaper.getWallpapers()
                currentWallpapers.add(id_live_wallpaper)

                sharedPreferencesLiveWallpaper.saveWallpapers(currentWallpapers)

                CommonObject.isFavoriteTrue(resources, binding.btnFavorite)
            }else{
                // Remove ID if it already exists
                sharedPreferencesLiveWallpaper.removeWallpaper(id_live_wallpaper)
                CommonObject.isFavoriteFalse(resources, binding.btnFavorite)
                Toast.makeText(mContext, "Live Wallpaper is duplicate!", Toast.LENGTH_SHORT).show()
            }

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

    }


    companion object {
        val TAG = DetailLiveWallpaperFragment::class.java.name
    }

}