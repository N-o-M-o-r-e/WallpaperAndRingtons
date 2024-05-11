package com.example.wallpagerandringtons.model.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    val BASE_URL = "http://app1.remimobile.com/api/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build())
            .build()
    }
}