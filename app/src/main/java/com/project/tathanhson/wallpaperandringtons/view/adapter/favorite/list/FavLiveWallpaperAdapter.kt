package com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.ItemWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.livewallpaper.LiveWallpaperItem

class FavLiveWallpaperAdapter(
    private val context: Context,
    private var liveWallpapers: ArrayList<LiveWallpaperItem>
) : RecyclerView.Adapter<FavLiveWallpaperAdapter.LiveWallpaperViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveWallpaperViewHolder {
        val binding =
            ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LiveWallpaperViewHolder(binding)
    }

    override fun getItemCount(): Int = liveWallpapers.size

    override fun onBindViewHolder(holder: LiveWallpaperViewHolder, position: Int) {
        val liveWallpaper = liveWallpapers[position]
        CommonObject.loadPathImageToView(
            context,
            liveWallpaper.img_large,
            holder.binding.imgWallpaper
        )
        holder.binding.tvFavorite.text = liveWallpaper.favorite.toString()
    }

    inner class LiveWallpaperViewHolder(val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                // Handle item click if needed
            }
        }
    }

    // Function to update the list of live wallpapers and notify the adapter
    fun updateLiveWallpapers(newLiveWallpapers: ArrayList<LiveWallpaperItem>) {
        liveWallpapers = newLiveWallpapers
        notifyDataSetChanged()
    }
}