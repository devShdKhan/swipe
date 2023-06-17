package com.example.swipeassignment.di

import com.example.swipeassignment.network.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkService {

    private const val BASE_URL = "https://app.getswipe.in/"

    @Singleton
    @Provides
    fun getApiInterface(retrofit: Retrofit) = retrofit.create(ProductApi::class.java)

    @Singleton
    @Provides
    fun getRetrofit() = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}