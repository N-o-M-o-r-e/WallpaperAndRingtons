package com.project.tathanhson.wallpaperandringtons.view.adapter.favorite

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.project.tathanhson.mediaplayer.model.Data
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailRingtonesBinding
import com.project.tathanhson.wallpaperandringtons.utils.ShareManager
import com.project.tathanhson.wallpaperandringtons.utils.SoundSettingManager
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DetailRingtoneFavFragment : BaseFragment<FragmentDetailRingtonesBinding>(FragmentDetailRingtonesBinding::inflate) {
    lateinit var viewModel: RingtonesVM
    private var mediaPlayer: MediaPlayer? = null
    private var listRingtone = ArrayList<Data>()
    private lateinit var url :String
    private lateinit var data: Data
    private lateinit var soundSettingManager: SoundSettingManager
    private var index: Int = -1
    private var playbackProgressJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (context is AppCompatActivity) {
            var requestPermissionLauncher: ActivityResultLauncher<String>
            requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    soundSettingManager.setAlarmSound(url, RingtoneManager.TYPE_RINGTONE)
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            soundSettingManager = SoundSettingManager(context as AppCompatActivity, requestPermissionLauncher)
        } else {
            throw IllegalArgumentException("Context must be an AppCompatActivity")
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            var requestPermissionLauncher: ActivityResultLauncher<String>
            requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    soundSettingManager.setAlarmSound(url, RingtoneManager.TYPE_RINGTONE)
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            soundSettingManager = SoundSettingManager(context, requestPermissionLauncher)
        } else {
            throw IllegalArgumentException("Context must be an AppCompatActivity")
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[RingtonesVM::class.java]

    }

    @SuppressLint("LogConditional")
    override fun initData() {
        CommonObject.favoriteRingtones.observe(viewLifecycleOwner) { listRingtone ->
            this.listRingtone = listRingtone
        }

        CommonObject.positionRingtoneFav.observe(viewLifecycleOwner) { position ->

            position?.let {
                data = listRingtone[position]
                binding.tvName.text = data.name
                binding.tvTime.text = data.time
                CommonObject.checkFavoriteRingtoneUI(data, sharedPreferencesRingtones,resources,binding.btnFavorite)
                binding.seekBar.max = formatTimeToInt(data.time)
                url = data.link
                playMediaRingtone(url)
                index = position
                viewPlay()
            }
        }

    }

    override fun initView() {
        viewRingtone()
        mediaRingtone()
        utilsRingtone()
        setAsRingtone()
    }

    private fun setAsRingtone() {

        binding.btnAlarm.setOnClickListener {
            soundSettingManager.setAlarmSound(url, RingtoneManager.TYPE_ALARM)
            Log.d(TAG, "btnAlarm: url "+url)
        }
        binding.btnNotification.setOnClickListener {
            soundSettingManager.setAlarmSound(url, RingtoneManager.TYPE_NOTIFICATION)
            Log.d(TAG, "btnNotification: url "+url)
        }
        binding.btnRingtones.setOnClickListener {
            soundSettingManager.setAlarmSound(url, RingtoneManager.TYPE_RINGTONE)
            Log.d(TAG, "btnRingtones: url "+url)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        soundSettingManager.onActivityResult(requestCode, resultCode, data, url, RingtoneManager.TYPE_RINGTONE)
    }



    private fun viewRingtone() {
        binding.btnClose.setOnClickListener {
            requireActivity().finish()
            CommonObject.positionRingtoneFav.value = null
        }
    }

    private fun utilsRingtone() {
        binding.btnFavorite.setOnClickListener {
            if (!sharedPreferencesRingtones.isRingtoneExist(url)) {
                val listUrlPref = sharedPreferencesRingtones.getRingtones()
                listUrlPref.add(data)
                sharedPreferencesRingtones.saveRingtones(listUrlPref)
                CommonObject.isFavoriteTrue(resources, binding.btnFavorite)
            } else {
                sharedPreferencesRingtones.removeRingtone(data.link)
                CommonObject.isFavoriteFalse(resources, binding.btnFavorite)
                Toast.makeText(requireActivity(), "Ringtone removed from favorites!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(url))
            }
            startActivity(Intent.createChooser(shareIntent, "Share audio using"))
        }

        binding.btnDownload.setOnClickListener {
            Toast.makeText(mContext, "Downloading...", Toast.LENGTH_SHORT).show()
            checkPermissionsAndDownloadAudio()

        }
    }

    private fun checkPermissionsAndDownloadAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 và cao hơn
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                    REQUEST_READ_MEDIA_AUDIO
                )
            } else {
                downloadAudio()
            }
        } else { // Android 12 và thấp hơn
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE
                )
            } else {
                downloadAudio()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_MEDIA_AUDIO, REQUEST_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadAudio()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun downloadAudio() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.connect()

                val inputStream: InputStream = urlConnection.inputStream
                val filename = "ringtone_${System.currentTimeMillis()}.mp3"

                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, filename)
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Download complete", Toast.LENGTH_SHORT).show()
                    // Scan the file so it appears in the library
                    MediaScannerConnection.scanFile(
                        requireContext(),
                        arrayOf(file.absolutePath),
                        arrayOf("audio/mp3")
                    ) { path, uri ->
                        Log.d(TAG, "File scanned into media library: $path")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun mediaRingtone() {
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
    }


    fun indexMediaRingtone(index: Int) {
        if (listRingtone.size-1 >=index && index >=0) {
            CommonObject.positionRingtoneFav.value = index
        }
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
        private const val REQUEST_WRITE_STORAGE = 112
        private const val REQUEST_READ_MEDIA_AUDIO = 113
    }

}