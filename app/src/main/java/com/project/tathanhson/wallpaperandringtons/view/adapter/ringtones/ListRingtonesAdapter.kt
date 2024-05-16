package com.project.tathanhson.wallpaperandringtons.view.adapter.ringtones

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.ItemRingtoneBinding
import com.project.tathanhson.wallpaperandringtons.model.ringtones.Ringtone
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM
import java.io.IOException

class ListRingtonesAdapter(
    private val context: Context,
    private val viewModel: RingtonesVM,
    private val lifecycleOwner: LifecycleOwner,
    private val ringtonesList: ArrayList<Ringtone>,
    private var title: String
) : RecyclerView.Adapter<ListRingtonesAdapter.ItemHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var selectedPosition: Int = -1


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
                    val position = adapterPosition
                    val itemSelected = ringtonesList[position]
                    CommonObject.positionItemRingtonw.value = position
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemRingtoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount() : Int {
        return ringtonesList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemRingtone = ringtonesList[position]

        val randomBackground = backgroundResources.random()
        holder.binding.viewRingtone.setBackgroundResource(randomBackground)

        holder.binding.tvName.text = itemRingtone.fileName

        holder.binding.btnPlayOrPause.setOnClickListener {
            handleItemClick(position)
        }

        CommonObject.itemTitleRingtone.observe(lifecycleOwner, Observer { titleChange ->
            if (!titleChange.equals(title)) {
                stopRingtone()
            }
        })

        // Cập nhật trạng thái của nút phát/pause
        val iconResId = if (selectedPosition == position) {
            if (mediaPlayer?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
        } else {
            R.drawable.ic_play
        }
        holder.binding.btnPlayOrPause.setImageResource(iconResId)
    }

    private fun handleItemClick(position: Int) {
        if (selectedPosition != position) {
            // Ngừng nhạc của item hiện tại (nếu có)
            stopRingtone()
            selectedPosition = position
            // Phát nhạc của item mới
            playRingtone(context, ringtonesList[position])
        } else {
            // Nếu đang phát nhạc, dừng lại; nếu không, tiếp tục phát
            if (mediaPlayer?.isPlaying == true) {
                stopRingtone()
            } else {
                playRingtone(context, ringtonesList[position])
            }
        }
        notifyDataSetChanged()
    }

    private fun stopRingtone() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun playRingtone(context: Context, ringtone: Ringtone) {
        stopRingtone()
        try {
            val assetFileDescriptor = context.assets.openFd("${ringtone.folderName}/${ringtone.fileName}")
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            mediaPlayer?.setOnCompletionListener {
                selectedPosition = -1
                notifyDataSetChanged()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

