package com.example.wallpagerandringtons.view.adapter.wallpapers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.wallpagerandringtons.model.WallpaperItem
import com.example.wallpagerandringtons.model.WallpaperList
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ItemWallpaperBinding

class DesWallpaperAdapter(
    private val context: Context,
    private val viewModel: WallpaperMV,
    private val lifecycleOwner: LifecycleOwner,
    private val wallpaperList: WallpaperList

) : RecyclerView.Adapter<DesWallpaperAdapter.ItemHolder>() {

    private var itemSelect: WallpaperItem? = null

    inner class ItemHolder(val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemSelect = itemView.tag as WallpaperItem
                itemSelect.let {

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return wallpaperList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        // gán dữ liệu detail item
        val itemWallpaper = wallpaperList.get(position)
        val imgPath = itemWallpaper.img_large
        loadPathImageToView(imgPath, holder.binding.imgWallpaper)
    }

    private fun loadPathImageToView(pathImage: String, imgWallpaper: ImageView) {
        Glide.with(context)
            .load(pathImage)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.img_placeholder) // Ảnh placeholder nếu URL không hợp lệ hoặc không thể tải được
                    .error(R.drawable.img_placeholder) // Ảnh hiển thị khi có lỗi xảy ra trong quá trình tải ảnh
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            ) // Cấu hình cho việc sử dụng cache
            .into(imgWallpaper)
    }
}