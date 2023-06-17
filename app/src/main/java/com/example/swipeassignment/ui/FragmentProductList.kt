package com.example.swipeassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.swipeassignment.R
import com.example.swipeassignment.adapter.ProductAdapter
import com.example.swipeassignment.databinding.FragmentProductListBinding
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.model.Resource
import com.example.swipeassignment.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentProductList : Fragment() {

    private val viewModel by viewModels<ProductViewModel>()
    private val productAdapter by lazy { ProductAdapter() }
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.addTextChangedListener {
            binding.imgClearQuery.isVisible = it.toString().isNotEmpty()
            viewModel.searchProducts(it.toString())
        }

        binding.imgClearQuery.setOnClickListener { binding.etSearch.setText("") }

        binding.rvProducts.adapter = productAdapter

        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_addProductFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect {
                when (it) {
                    is Resource.Error -> setupError(it.message)
                    is Resource.Success -> setupSuccess(it.data)
                    is Resource.Loading -> setupLoading()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredList.collect { productAdapter.submitList(it) }
        }

    }

    private fun setupLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun setupError(message: String) {
        binding.progress.visibility = View.GONE
        binding.txtError.text = message
    }

    private fun setupSuccess(data: List<Product>) {
        viewModel.setMainList(data)
        binding.progress.visibility = View.GONE
        productAdapter.submitList(data)
    }
}