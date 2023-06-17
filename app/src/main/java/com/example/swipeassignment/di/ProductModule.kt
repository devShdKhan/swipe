package com.example.swipeassignment.di

import com.example.swipeassignment.repository.ProductRepository
import com.example.swipeassignment.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductModule {

    @Binds
    abstract fun bindProductRepository(quoteRepositoryImpl: ProductRepositoryImpl): ProductRepository

}