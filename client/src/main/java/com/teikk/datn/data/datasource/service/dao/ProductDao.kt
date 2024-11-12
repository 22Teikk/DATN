package com.teikk.datn.data.datasource.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.teikk.datn.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProducts(products: List<Product>)

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM product_tables")
    fun getProduct(): List<Product>

    @Query("SELECT * FROM product_tables")
    fun getProductAsFlow(): Flow<List<Product>>

    @Query("SELECT * FROM product_tables WHERE id = :id")
    fun getProductById(id: String): Product?

    @Delete
    fun deleteProduct(product: Product)
}