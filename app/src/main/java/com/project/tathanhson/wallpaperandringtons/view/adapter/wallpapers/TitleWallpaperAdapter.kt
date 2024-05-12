package com.example.wallpagerandringtons.view.adapter.wallpapers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpagerandringtons.model.WallpaperItem
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.project.tathanhson.wallpaperandringtons.databinding.ItemTitleBinding
import com.project.tathanhson.wallpaperandringtons.model.TitleWallpapers

class TitleWallpaperAdapter(
    private val context: Context,
    private val viewModel: WallpaperMV,
    private val lifecycleOwner: LifecycleOwner,
    private val listTitle: ArrayList<TitleWallpapers>
) : RecyclerView.Adapter<TitleWallpaperAdapter.ItemHolder>() {
    var itemSelect: WallpaperItem? = null

    inner class ItemHolder(val binding: ItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemSelect = itemView.tag as WallpaperItem
                itemSelect.let {

                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TitleWallpaperAdapter.ItemHolder {
        val binding =
            ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTitle.size
    }

    override fun onBindViewHolder(holder: TitleWallpaperAdapter.ItemHolder, position: Int) {
        val itemTitle = listTitle[position]
        holder.binding.btnTitle.text = itemTitle.title
    }
}