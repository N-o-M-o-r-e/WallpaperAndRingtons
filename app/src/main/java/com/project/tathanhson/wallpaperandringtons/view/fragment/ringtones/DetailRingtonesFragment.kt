package com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailRingtonesBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DetailRingtonesFragment :
    BaseFragment<FragmentDetailRingtonesBinding>(FragmentDetailRingtonesBinding::inflate) {
        lateinit var viewModel: RingtonesVM
    private var mediaPlayer: MediaPlayer? = null
    private var listRingtone = ArrayList<Data>()
    private var url = ""

    private var index: Int = -1
    private var playbackProgressJob: Job? = null

    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[RingtonesVM::class.java]
    }

    @SuppressLint("LogConditional")
    override fun initData() {
        CommonObject.listDataRingtone.observe(viewLifecycleOwner) { listRingtone ->
            this.listRingtone = listRingtone

        }
        CommonObject.positionDataRingtone.observe(viewLifecycleOwner) { position ->
            binding.tvName.text = listRingtone[position].name
            binding.tvTime.text = listRingtone[position].time
            binding.seekBar.max = formatTimeToInt(listRingtone[position].time)
            Log.d(TAG, "initData: " + formatTimeToInt(listRingtone[position].time))
            url = listRingtone[position].link
            playMediaRingtone(url)
            index = position
            viewPlay()
        }

    }


    override fun initView() {
        binding.btnPlay.setOnClickListener {
            viewPlay()
            playMediaRingtone(url)
        }

        binding.btnPause.setOnClickListener {
            viewPause()
            pauseMediaRingtone()
        }

        binding.btnNext.setOnClickListener {
            viewPause()
            stopMediaRingtone()
            index++
            indexMediaRingtone(index)
        }

        binding.btnBack.setOnClickListener {
            viewPause()
            stopMediaRingtone()
            index--
            indexMediaRingtone(index)
        }

        binding.btnClose.setOnClickListener {
            requireActivity().finish()
        }

    }

    fun indexMediaRingtone(index: Int) {
        CommonObject.positionDataRingtone.value = index
    }

    @SuppressLint("SetTextI18n")
    fun playMediaRingtone(url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                    listenerSeekbar(it)
                }
                setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                    viewPause()
                    binding.seekBar.progress = 0
                    binding.tvCountTime.text = "00:00"
                }
                prepareAsync()
            }
        } else {
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer?.start()
                if (playbackProgressJob == null || playbackProgressJob?.isCancelled == true) {
                    startSeekBarUpdateCoroutine(mediaPlayer!!.duration)
                }
            } else {
                // If media is playing, just restart the seek bar update coroutine
                if (playbackProgressJob?.isCancelled == true) {
                    startSeekBarUpdateCoroutine(mediaPlayer!!.duration)
                }
            }
        }
    }



    private fun listenerSeekbar(mediaPlayer: MediaPlayer) {
        startSeekBarUpdateCoroutine(mediaPlayer.duration)

        binding.seekBar.isEnabled = false

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    @SuppressLint("LogConditional")
    private fun startSeekBarUpdateCoroutine(duration: Int) {
        var currentPosition = 0
        playbackProgressJob?.cancel()

        Log.d(TAG, "startSeekBarUpdateCoroutine: $duration")
        playbackProgressJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive && mediaPlayer != null ) {
                currentPosition = mediaPlayer!!.currentPosition
                binding.seekBar.progress = currentPosition
                binding.tvCountTime.text = formatTime(currentPosition)
                delay(10)
            }
        }
    }



    @SuppressLint("DefaultLocale")
    private fun formatTime(duration: Int): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun formatTimeToInt(time: String): Int {
        val parts = time.split(":")
        val minutes = parts[0].toInt()
        val seconds = parts[1].toInt()
        return (minutes * 60 + seconds) * 1000
    }


    fun pauseMediaRingtone() {
        mediaPlayer?.pause()

    }

    @SuppressLint("SetTextI18n")
    fun stopMediaRingtone() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer = null
        viewPause()
        binding.seekBar.progress = 0
        binding.tvCountTime.text = "00:00"
    }


    fun viewPlay() {
        binding.btnPlay.visibility = View.GONE
        binding.btnPause.visibility = View.VISIBLE
    }

    fun viewPause() {
        binding.btnPlay.visibility = View.VISIBLE
        binding.btnPause.visibility = View.GONE
    }


    override fun onPause() {
        super.onPause()
        stopMediaRingtone()
    }

    override fun onStop() {
        super.onStop()
        stopMediaRingtone()
    }

    companion object {
        val TAG = "AAAAAAAAAAAAAAAAAA"
    }

}