package com.project.tathanhson.mediaplayer.model

import java.io.Serializable

data class Data(
    val link: String,
    val name: String,
    val size: String,
    val time: String
) : Serializable