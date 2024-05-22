package com.example.wallpagerandringtons.view.adapter.wallpapers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Wallpapers
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.ItemWallpaperBinding

class ListWallpaperAdapter(
    private val context: Context,
    private val viewModel: WallpaperVM,
    private val lifecycleOwner: LifecycleOwner,
    private val wallpaperList: Wallpapers

) : RecyclerView.Adapter<ListWallpaperAdapter.ItemHolder>() {


    private var itemSelect: WallpaperItem? = null

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
        var imgPath = itemWallpaper.img_thumb
        Log.d("API", "onBindViewHolder: "+itemWallpaper.img_large)

        if (itemWallpaper.img_thumb == null){
            imgPath = ""
        }else{
            CommonObject.loadPathImageToView(context, imgPath, holder.binding.imgWallpaper)
        }

        holder.binding.tvFavorite.text = itemWallpaper.favorite.toString()

        holder.itemView.tag = position

    }

    inner class ItemHolder(val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = itemView.tag as Int

                itemSelect = wallpaperList[position]
                CommonObject.positionItemWallpaper.value = position

                itemSelect?.let {
                    CommonObject.itemWallpaper.value = itemSelect
                }
            }
        }
    }

}