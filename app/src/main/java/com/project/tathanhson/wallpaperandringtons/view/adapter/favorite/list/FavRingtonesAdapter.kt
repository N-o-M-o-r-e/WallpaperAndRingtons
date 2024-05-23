package com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.databinding.ItemRingtoneFavBinding

class FavRingtonesAdapter(
    private val context: Context,
    private var ringtones: ArrayList<Data>
) : RecyclerView.Adapter<FavRingtonesAdapter.RingtoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingtoneViewHolder {
        val binding =
            ItemRingtoneFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RingtoneViewHolder(binding)
    }

    override fun getItemCount(): Int = ringtones.size

    override fun onBindViewHolder(holder: RingtoneViewHolder, position: Int) {
        holder.binding.tvRingtone.text = ringtones[position].name

    }

    inner class RingtoneViewHolder(val binding: ItemRingtoneFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {

            }
        }
    }

    fun updateRingtones(newRingtones: ArrayList<Data>) {
        ringtones = newRingtones
        notifyDataSetChanged()
    }
}