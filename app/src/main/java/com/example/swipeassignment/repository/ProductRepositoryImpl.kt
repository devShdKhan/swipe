package com.example.swipeassignment.repository

import com.example.swipeassignment.model.AddProductResponse
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.model.Resource
import com.example.swipeassignment.network.ProductApi
import com.example.swipeassignment.utils.toMultipartBody
import com.example.swipeassignment.utils.toRequestBody
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiInterface: ProductApi
) : ProductRepository {

    override fun fetchProducts() = flow {
        try {
            val response = apiInterface.getProducts()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun saveProduct(product: Product, image: File?): Resource<AddProductResponse> {
        return try {
            val response = apiInterface.saveProducts(
                name = product.product_name.toRequestBody(),
                type = product.product_type.toRequestBody(),
                price = product.price.toString().toRequestBody(),
                tax = product.tax.toString().toRequestBody(),
                image = image?.toMultipartBody()
            )
            if (response.success) {
                Resource.Success(response)
            } else {
                Resource.Error(response.message)
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

}