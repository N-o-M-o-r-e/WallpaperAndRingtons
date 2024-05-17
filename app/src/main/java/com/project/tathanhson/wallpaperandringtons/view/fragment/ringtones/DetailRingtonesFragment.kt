package com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailRingtonesBinding
import com.project.tathanhson.wallpaperandringtons.model.ringtones.Ringtone
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class DetailRingtonesFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentDetailRingtonesBinding
    private lateinit var viewModel: RingtonesVM
    private var mediaPlayer: MediaPlayer? = null
    private var listRingtone = ArrayList<Ringtone>()
    private var position = 0
    private val isPlay = true
    private var playbackProgressJob: Job? = null
    private var isUserSeeking = false

    companion object {
        val TAG = "AAAAAAAAAAAAAAAAA"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRingtonesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RingtonesVM::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createData()
        initData()
    }

    private fun createData() {
        CommonObject.listRingtone.observe(viewLifecycleOwner, Observer { listRingtone ->
            this.listRingtone = listRingtone
        })
        CommonObject.positionItemRingtone.observe(viewLifecycleOwner, Observer { position ->
            this.position = position
            binding.tvName.text = listRingtone[position].fileName
            playRingtone(context, listRingtone[position])
        })
    }

    private fun initData() {
        binding.btnPlayOrPause.setOnClickListener {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                binding.btnPlayOrPause.setImageResource(R.drawable.img_play)
                playbackProgressJob?.cancel() // Hủy coroutine khi tạm dừng nhạc
            } else {
                mediaPlayer?.start()
                binding.btnPlayOrPause.setImageResource(R.drawable.img_pause)
                startPlaybackProgressUpdater() // Bắt đầu cập nhật seekbar khi phát lại nhạc
            }
        }

        binding.btnBack.setOnClickListener {

        }

        binding.btnNext.setOnClickListener {

        }

    }

    private fun playRingtone(context: Context, ringtone: Ringtone) {
        stopRingtone()
        try {
            val assetFileDescriptor =
                context.assets.openFd("${ringtone.folderName}/${ringtone.fileName}")
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length
            )
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                binding.seekBar.progress = 0
            }
            val duration = mediaPlayer!!.duration
            binding.seekBar.max = duration
            binding.tvTime.text = formatTime(duration)
            startPlaybackProgressUpdater()


            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) mediaPlayer?.seekTo(progress)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}

            })

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun startPlaybackProgressUpdater() {
        playbackProgressJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                val progress = mediaPlayer!!.currentPosition
                Log.e(TAG,   "progress "+formatTime(progress) + "/ duration: "+formatTime(mediaPlayer!!.duration) )
                binding.seekBar.progress = progress
                binding.tvCountTime.text = formatTime(progress)
                delay(100)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(duration: Int): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun stopRingtone() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        stopRingtone()
    }

}