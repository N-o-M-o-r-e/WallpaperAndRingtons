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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initData() {
        CommonObject.itemWallpaper.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                imagePath = it.img_large
                CommonObject.loadPathImageToView(mContext, imagePath, binding.imgDetail)
//                binding.btnFavorite.text = it.favorite.toString()
//                binding.btnDownload.text = it.download.toString()
//                countFavorite = it.favorite

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

//            countFavorite++
            CommonObject.itemWallpaper.observe(viewLifecycleOwner) { item ->
                item?.let {
                    val id_wallpaper = item.id

                    if (!sharedPreferencesWallpaper.isIdExist(id_wallpaper)) {
                        // Update favorite to API
                        viewModel.postUpdateFavorite(id_wallpaper)
//                        binding.btnFavorite.text = countFavorite.toString()
//                        CommonObject.listWallpaperFav.add(it)
                        // Lưu ID vào SharedPreferences

                        val currentWallpapers = sharedPreferencesWallpaper.getWallpapers()
                        currentWallpapers.add(id_wallpaper)
                        sharedPreferencesWallpaper.saveWallpapers(currentWallpapers)
                        CommonObject.isFavoriteTrue(resources, binding.btnFavorite)

                    } else {
                        // ID đã tồn tại, có thể thông báo hoặc thực hiện xử lý khác tùy thuộc vào yêu cầu của ứng dụng
                        Log.e("AAAAAAAAA", "ID đã tồn tại trong SharedPreferences")
                    }
                }
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