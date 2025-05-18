package com.vito.tiprankshometask.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: StockApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://trautocomplete.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StockApiService::class.java)
    }
}