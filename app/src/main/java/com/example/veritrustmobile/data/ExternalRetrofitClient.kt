package com.example.veritrustmobile.data

import com.example.veritrustmobile.model.MindicadorResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ExternalApiService {
    @GET("api")
    suspend fun getIndicadores(): MindicadorResponse
}

object ExternalRetrofitClient {
    private const val BASE_URL = "https://mindicador.cl/"

    val api: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}