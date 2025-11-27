package com.example.veritrustmobile.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.veritrustmobile.data.ApiService

object RetrofitClient {

    // CAMBIA EL PUERTO 8080 POR EL PUERTO DE TU API SI ES DIFERENTE
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}