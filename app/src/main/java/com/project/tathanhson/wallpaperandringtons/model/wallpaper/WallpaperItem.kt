package com.project.tathanhson.wallpaperandringtons.model.wallpaper

import java.io.Serializable

data class WallpaperItem(
    val category_id: Int,
    val date_upload: String,
    val download: Int,
    val favorite: Int,
    val id: Int,
    val img_large: String,
    val img_thumb: String,
    val live: Int,
    val name: String,
    val premium: Int
) : Serializable