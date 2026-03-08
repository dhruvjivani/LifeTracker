package com.conestoga.lifetracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var BASE_URL = "https://api.openweathermap.org/"
    private var retrofit: Retrofit? = null

    val instance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    /**
     * Used for testing purposes to point to MockWebServer
     */
    fun setBaseUrl(url: String) {
        BASE_URL = url
        retrofit = null // Reset to recreate with new URL
    }
}
