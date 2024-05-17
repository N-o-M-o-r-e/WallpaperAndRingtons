package com.project.tathanhson.wallpaperandringtons.view.adapter.ringtones

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ItemTitleBinding
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM

class TitleRingtonesAdapter(
    private val context: Context,
    private val viewModel: RingtonesVM,
    private val lifecycleOwner: LifecycleOwner,
    private val listTitle: ArrayList<String>
) : RecyclerView.Adapter<TitleRingtonesAdapter.ItemHolder>() {

    var selectedPosition: Int = 0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TitleRingtonesAdapter.ItemHolder {
        val binding = ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: TitleRingtonesAdapter.ItemHolder, position: Int) {
        val itemTitle = listTitle[position]
        holder.binding.btnTitle.text = itemTitle

        // Đặt màu văn bản dựa trên vị trí đã chọn
        if (position == selectedPosition) {
            holder.binding.btnTitle.setTextColor(Color.GREEN)
            holder.binding.btnTitle.setBackgroundResource(R.drawable.background_item_selected)
        } else {
            holder.binding.btnTitle.setTextColor(Color.GRAY)
            holder.binding.btnTitle.setBackgroundResource(R.drawable.background_item_unselect)
        }
    }

    override fun getItemCount(): Int {
        return listTitle.size
    }

    inner class ItemHolder(val binding: ItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewTitle.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = position
                    notifyItemChanged(selectedPosition)
                    val itemSelect = listTitle[position]
                    CommonObject.itemTitleRingtone.value = itemSelect
//                    Log.d("TitleWallpaperAdapter", "Selected title: ${itemSelect}")
                }
            }
        }
    }
}