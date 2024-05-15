package com.example.wallpagerandringtons.view.adapter.wallpapers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpagerandringtons.viewmodel.WallpaperMV
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ItemTitleBinding
import com.project.tathanhson.wallpaperandringtons.model.wallpaper.Title

class TitleWallpaperAdapter(
    private val context: Context,
    private val viewModel: WallpaperMV,
    private val lifecycleOwner: LifecycleOwner,
    private val listTitle: ArrayList<Title>
) : RecyclerView.Adapter<TitleWallpaperAdapter.ItemHolder>() {

    var selectedPosition: Int = 0

    inner class ItemHolder(val binding: ItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewTitle.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = position
                    notifyItemChanged(selectedPosition)
                    val itemSelect = listTitle[position]
                    viewModel.ldItemTitle.value = itemSelect
                    Log.d("TitleWallpaperAdapter", "Selected title: ${itemSelect.name}")
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TitleWallpaperAdapter.ItemHolder {
        val binding = ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTitle.size
    }

    @SuppressLint("LogConditional")
    override fun onBindViewHolder(holder: TitleWallpaperAdapter.ItemHolder, position: Int) {
        val itemTitle = listTitle[position]
        holder.binding.btnTitle.text = itemTitle.name

        // Đặt màu văn bản dựa trên vị trí đã chọn
        if (position == selectedPosition) {
            holder.binding.btnTitle.setTextColor(Color.GREEN)
            holder.binding.btnTitle.setBackgroundResource(R.drawable.background_item_selected)
        } else {
            holder.binding.btnTitle.setTextColor(Color.GRAY)
            holder.binding.btnTitle.setBackgroundResource(R.drawable.background_item_unselect)
        }
    }
}