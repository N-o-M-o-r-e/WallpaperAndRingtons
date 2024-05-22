package com.project.tathanhson.wallpaperandringtons.model.wallpaper

import java.io.Serializable

data class CategoryWallpaper(
    val id: Int,
    val name: String,
    val image_url : String
) : Serializable