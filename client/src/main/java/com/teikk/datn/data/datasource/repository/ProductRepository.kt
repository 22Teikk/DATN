package com.teikk.datn.data.datasource.repository

import android.util.Log
import com.teikk.datn.data.datasource.local.ProductLocalRepository
import com.teikk.datn.data.datasource.remote.ProductRemoteRepository
import com.teikk.datn.data.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteRepository: ProductRemoteRepository,
    private val productLocalRepository: ProductLocalRepository,
) {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products get()= _products
    init {
        fetchProductLocal()
        fetchProductRemote()
    }
    fun fetchProductLocal() = CoroutineScope(Dispatchers.IO).launch {
        productLocalRepository.getProductAsFlow().collect {
            _products.value = it
        }
    }

    fun fetchProductRemote() = CoroutineScope(Dispatchers.IO).launch {
        val response = productRemoteRepository.getAllProducts()
        if (response.isSuccessful) {
            val products = response.body()!!
            productLocalRepository.insertProducts(products)
        }
    }
}