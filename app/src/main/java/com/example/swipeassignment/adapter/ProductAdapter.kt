package com.example.swipeassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swipeassignment.databinding.ItemProductBinding
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.utils.load

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.toString() == newItem.toString()
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { product -> holder.bindProduct(product) }
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindProduct(product: Product) {
            binding.apply {
                txtProductName.text = product.product_name
                imgProduct.load(product.image)
                txtProductType.text = product.product_type
                txtProductTax.text = "Tax Applicable ${product.tax}%"
                txtProductPrice.text = "Price ₹${product.price}"
            }
        }
    }

}