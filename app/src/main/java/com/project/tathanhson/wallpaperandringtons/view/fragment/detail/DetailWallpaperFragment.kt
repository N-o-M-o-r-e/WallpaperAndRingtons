package com.project.tathanhson.wallpaperandringtons.view.fragment.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
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
    lateinit var viewModel: WallpaperMV

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
            item?.let {
                imagePath = it.img_thumb
                loadPathImageToView(imagePath, binding.imgDetail)
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
//            checkPermission()
            requestStoragePermission()

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


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(
                    requireActivity(),
                    "permissions have been granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireActivity(), "Permission don't sucsess", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireActivity(),
                    "permissions have been granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireActivity(), "Permission don't sucsess", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("AAAAAAAAAAAAAA", "requestStoragePermission: true")
                //Quyền cấp thành công
            } else {
                // Yêu cầu cấp quyền
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE),REQUEST_CODE)
                requestManageStoragePermission()
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Log.e("AAAAAAAAAAAAAA", "requestStoragePermission: true")
                //Quyền cấp thành công
            } else {
                // Yêu cầu cấp quyền
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_CODE)
            }
        }

    }

    var requestManageStoragePermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult? ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {

            } else {
                Toast.makeText(
                    requireActivity(),
                    "Allow permission for storage access!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun requestManageStoragePermission() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data =
                Uri.parse(String.format("package:%s", requireActivity().packageName))
            requestManageStoragePermissionResultLauncher.launch(intent)
        } catch (e: Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            requestManageStoragePermissionResultLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= 30) {
                if (Environment.isExternalStorageManager()) {

                } else {
                    requestStoragePermission()
                }
            } else {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    requestStoragePermission()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            if (Build.VERSION.SDK_INT >= 30) {
                if (Environment.isExternalStorageManager()) {

                } else {

                }

            } else {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                } else {

                }
            }
        }
    }
}