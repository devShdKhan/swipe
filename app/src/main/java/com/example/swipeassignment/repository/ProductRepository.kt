package com.example.swipeassignment.repository

import com.example.swipeassignment.model.AddProductResponse
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.model.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProductRepository {

    fun fetchProducts(): Flow<Resource<List<Product>>>

    suspend fun saveProduct(product: Product, image: File?): Resource<AddProductResponse>

}