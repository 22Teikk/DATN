package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.datasource.service.dao.ProductDao
import com.teikk.datn.data.model.Product
import javax.inject.Inject

class ProductLocalRepository @Inject constructor(
    private val productDao: ProductDao
) {
    suspend fun insertProducts(products: List<Product>) = productDao.insertProducts(products)
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    suspend fun getProduct() = productDao.getProduct()
    suspend fun getProductAsFlow() = productDao.getProductAsFlow()
    suspend fun getProductById(id: String) = productDao.getProductById(id)
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
}