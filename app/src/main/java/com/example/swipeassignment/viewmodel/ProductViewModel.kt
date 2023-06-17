package com.example.swipeassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.model.Resource
import com.example.swipeassignment.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(repository: ProductRepository) : ViewModel() {

    private var mainList = listOf<Product>()

    private val _filteredList = MutableStateFlow(listOf<Product>())
    val filteredList = _filteredList.asStateFlow()

    val products = repository.fetchProducts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Resource.Loading()
    )

    fun setMainList(list: List<Product>) {
        mainList = list
    }

    fun searchProducts(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val list = mainList.filter { it.product_name.contains(query, ignoreCase = true) }
            _filteredList.emit(list)
        }
    }
}