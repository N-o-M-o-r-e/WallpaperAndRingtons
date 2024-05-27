package com.project.tathanhson.wallpaperandringtons.view.adapter.live_wallpaper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.ItemLiveWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.databinding.ItemWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem
import com.project.tathanhson.wallpaperandringtons.viewmodel.LiveWallpaperVM

class LiveWallpaperAdapter(
    private val context: Context,
    private val viewModel: LiveWallpaperVM,
    private val lifecycleOwner: LifecycleOwner,
    private val liveWallpapers: ArrayList<LiveWallpaperItem>
) : RecyclerView.Adapter<LiveWallpaperAdapter.ItemHolder>() {

    private var itemSelect: LiveWallpaperItem? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemLiveWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return liveWallpapers.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLiveWallpaper = liveWallpapers[position]
        val imgPath = itemLiveWallpaper.img_thumb

        CommonObject.loadPathImageToView(context, imgPath, holder.binding.imgWallpaper)

//        holder.binding.imgWallpaper.setVideoPath(imgPath)
//        holder.binding.imgWallpaper.start()
//        holder.binding.imgWallpaper.setOnCompletionListener { mediaPlayer ->
//            mediaPlayer?.let {
//                it.seekTo(0)
//                it.start()
//            }
//        }
        holder.binding.tvFavorite.text = itemLiveWallpaper.favorite.toString()
        holder.itemView.tag = itemLiveWallpaper
    }

    inner class ItemHolder(val binding: ItemLiveWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemSelect = itemView.tag as LiveWallpaperItem
                CommonObject.itemLiveWallpaper.value = itemSelect
            }
        }
    }
}