package com.project.tathanhson.wallpaperandringtons.view.adapter.favorite.detail

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ItemRingtoneBinding
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM

class FavRingtoneListAdapter(
    private val context: Context,
    private val viewModel: RingtonesVM,
    private val lifecycleOwner: LifecycleOwner,
    private var dataRingtoneList: ArrayList<Data>,
    private var title: String
) : RecyclerView.Adapter<FavRingtoneListAdapter.ItemHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition = MutableLiveData<Int>(-1)
    private var backgroundIndex = 0

    private val backgroundResources = arrayOf(
        R.drawable.background_ringtone_lavender,
        R.drawable.background_ringtone_blue,
        R.drawable.background_ringtone_blue_light,
        R.drawable.background_ringtone_oranger,
        R.drawable.background_ringtone_pink
    )

    inner class ItemHolder(val binding: ItemRingtoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

                CommonObject.positionRingtoneFav.value = adapterPosition

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemRingtoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataRingtoneList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemRingtone = dataRingtoneList[position]
        val url = itemRingtone.link

        val backgroundResource = backgroundResources[backgroundIndex % backgroundResources.size]
        holder.binding.viewRingtone.setBackgroundResource(backgroundResource)

        // Increase the background index
        backgroundIndex++

        holder.binding.tvName.text = itemRingtone.name
        holder.binding.tvSize.text = itemRingtone.size
        holder.binding.tvTime.text = itemRingtone.time

        currentPosition.observe(lifecycleOwner) { currentPosition ->
            currentPosition?.let {
                if (currentPosition.equals(position)) {
                    viewPause(holder)
                } else {
                    viewPause(holder)
                }
            }
        }

        holder.binding.btnPlay.setOnClickListener {
            currentPosition.value = position
            stopMediaRingtone()
            viewPlay(holder)
            playMediaRingtone(url)
        }

        holder.binding.btnPause.setOnClickListener {
            viewPause(holder)
            stopMediaRingtone()
            currentPosition.value = -1
        }

        CommonObject.categoryRingtone.observe(lifecycleOwner){ titleSelect ->
            if (title != "") {
                if (!titleSelect.equals(title)) {
                    stopMediaRingtone()
                    viewPause(holder)
                }
            }
        }
    }


    fun playMediaRingtone(url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                }
                setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                prepareAsync()
            }

        } else {
            mediaPlayer?.start()
        }
    }

    fun stopMediaRingtone() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun viewPlay(holder: ItemHolder) {
        holder.binding.btnPlay.visibility = View.GONE
        holder.binding.btnPause.visibility = View.VISIBLE
    }

    fun viewPause(holder: ItemHolder) {
        holder.binding.btnPlay.visibility = View.VISIBLE
        holder.binding.btnPause.visibility = View.GONE
    }

    fun updateRingtones(newRingtones: ArrayList<Data>) {
        dataRingtoneList = newRingtones
        notifyDataSetChanged()
    }
}
