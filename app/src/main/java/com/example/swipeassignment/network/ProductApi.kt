package com.example.swipeassignment.network

import com.example.swipeassignment.model.AddProductResponse
import com.example.swipeassignment.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductApi {

    @GET("/api/public/get")
    suspend fun getProducts(): List<Product>

    @Multipart
    @POST("/api/public/add")
    suspend fun saveProducts(
        @Part("product_name") name: RequestBody,
        @Part("product_type") type: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part image: MultipartBody.Part?
    ): AddProductResponse

}