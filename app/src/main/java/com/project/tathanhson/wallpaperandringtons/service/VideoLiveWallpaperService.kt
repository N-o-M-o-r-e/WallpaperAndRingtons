package com.project.tathanhson.wallpaperandringtons.service

import android.media.MediaPlayer
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.SurfaceHolder
import com.project.tathanhson.wallpaperandringtons.CommonObject

class VideoLiveWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return VideoEngine()
    }

    private inner class VideoEngine : Engine(), SurfaceHolder.Callback {
        private var mediaPlayer: MediaPlayer? = null
        private var surfaceHolder: SurfaceHolder? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            this.surfaceHolder = surfaceHolder
            surfaceHolder.addCallback(this)
        }

        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer?.release()
            mediaPlayer = null
            surfaceHolder?.removeCallback(this)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                mediaPlayer?.start()
            } else {
                mediaPlayer?.pause()
            }
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer().apply {
                        setSurface(holder.surface)
                        setDataSource(CommonObject.URL_LIVE_WALLPAPER)
                        Log.e("AAAAAAAAAAA", "surfaceCreated: "+CommonObject.URL_LIVE_WALLPAPER, )
                        isLooping = true
                        prepare()
                        start()
                    }
                } else {
                    mediaPlayer?.setSurface(holder.surface)
                    mediaPlayer?.start()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            mediaPlayer?.pause()
        }
    }
}

