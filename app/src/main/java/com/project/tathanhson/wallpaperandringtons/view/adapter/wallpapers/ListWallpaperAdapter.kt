package com.example.wallpagerandringtons.view.adapter.wallpapers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperList
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ItemWallpaperBinding

class ListWallpaperAdapter(
    private val context: Context,
    private val viewModel: WallpaperVM,
    private val lifecycleOwner: LifecycleOwner,
    private val wallpaperList: WallpaperList

) : RecyclerView.Adapter<ListWallpaperAdapter.ItemHolder>() {


    private var itemSelect: WallpaperItem? = null

    inner class ItemHolder(val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = itemView.tag as Int

                itemSelect = wallpaperList[position]
                CommonObject.positionItemWallpaper.value = position

                itemSelect?.let {
                    viewModel.ldItemWallpaper.value = itemSelect
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
        val itemWallpaper = wallpaperList[position]
        val imgPath = itemWallpaper.img_thumb
        Log.d("API", "onBindViewHolder: "+itemWallpaper.img_large)
        loadPathImageToView(imgPath, holder.binding.imgWallpaper)
        holder.binding.tvFavorite.text = itemWallpaper.favorite.toString()
        holder.itemView.tag = position

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