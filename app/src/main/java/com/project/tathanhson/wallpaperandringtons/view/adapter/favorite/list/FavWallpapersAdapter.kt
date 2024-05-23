package com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.ItemWallpaperBinding
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.WallpaperItem

class FavWallpapersAdapter(
    private val context: Context,
    private var wallpapers: ArrayList<WallpaperItem>
) : RecyclerView.Adapter<FavWallpapersAdapter.WallpaperViewHolder>() {

    override fun getItemCount(): Int = wallpapers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding =
            ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WallpaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val wallpaper = wallpapers[position]
        CommonObject.loadPathImageToView(context, wallpaper.img_large, holder.binding.imgWallpaper)
        holder.binding.tvFavorite.text = wallpaper.favorite.toString()
    }

    inner class WallpaperViewHolder(val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {

            }
        }
    }

    fun updateWallpapers(newWallpapers: ArrayList<WallpaperItem>) {
        wallpapers = newWallpapers
        notifyDataSetChanged()
    }
}
