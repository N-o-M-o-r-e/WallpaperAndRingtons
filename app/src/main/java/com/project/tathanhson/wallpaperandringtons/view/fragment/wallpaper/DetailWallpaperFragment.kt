package com.project.tathanhson.wallpaperandringtons.view.fragment.wallpaper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tathanhson.wallpaperandringtons.MyApplication
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.BottomDialogBinding
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.MainActivity

@Suppress("DEPRECATION")
class DetailWallpaperFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentDetailWallpaperBinding
    lateinit var viewModel: WallpaperVM
    private var countFavorite : Int =0
    private var coutClick : Int = 0

    private lateinit var imagePath: String
    private val REQUEST_CODE = 123

    companion object {
        val TAG = DetailWallpaperFragment::class.java.name
        const val flagHome: String = "HOME"
        const val flagLock: String = "LOCK"
        const val flagHomeAndLock: String = "HOME_AND_LOCK"
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(WallpaperVM::class.java)
        binding = FragmentDetailWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()

    }

    private fun initData() {
        CommonObject.iamgeWallperLD.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                imagePath = it.img_large
                loadPathImageToView(imagePath, binding.imgDetail)
                binding.btnFavorite.text = it.favorite.toString()
                binding.btnDownload.text = it.download.toString()

                countFavorite = it.favorite

            }

        })

        binding.btnClose.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }

        binding.imgDetail.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(context)
            val mBinding = BottomDialogBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(mBinding.root)
            bottomSheetDialog.show()
            listenerBottom(mBinding)

        }

        binding.btnFavorite.setOnClickListener {
//            Log.d("AAAAAAAAAAAAAAA", "initData: "+coutClick)
            if(coutClick == 0){
                countFavorite++
                coutClick++
//                Log.d("AAAAAAAAAAAAAA", "initData: click update")
                CommonObject.iamgeWallperLD.observe(viewLifecycleOwner, Observer { item ->
                    item?.let {
                        viewModel.postUpdateFavorite(it.id)
                        binding.btnFavorite.text = countFavorite.toString()
//                        Log.d("AAAAAAAAAAAAAAAAA", "initData: "+it.favorite.toString())

                    }
                })
            }
        }
    }

    private fun listenerBottom(mBinding: BottomDialogBinding) {
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