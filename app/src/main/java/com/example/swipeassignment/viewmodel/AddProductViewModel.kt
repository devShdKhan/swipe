package com.example.swipeassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.model.AddProductResponse
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.model.Resource
import com.example.swipeassignment.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _addProduct = MutableSharedFlow<Resource<AddProductResponse>>()
    val addProductFlow = _addProduct.asSharedFlow()

    fun saveProduct(product: Product, image: File? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _addProduct.emit(Resource.Loading())
            val (isValidProduct, message) = validateProduct(product)
            if (!isValidProduct) {
                _addProduct.emit(Resource.Error(message))
            } else {
                val response = repository.saveProduct(product, image)
                _addProduct.emit(response)
            }
        }
    }

    private fun validateProduct(product: Product): Pair<Boolean, String> {
        return if (product.product_name.isEmpty()) {
            false to "Please enter product name"
        } else if (product.product_type.isEmpty()) {
            false to "Please select product type"
        } else if (product.price == 0.0) {
            false to "Please enter product price"
        } else if (product.tax == 0.0 || product.tax > 100) {
            false to "Please valid product tax"
        } else true to ""
    }

}