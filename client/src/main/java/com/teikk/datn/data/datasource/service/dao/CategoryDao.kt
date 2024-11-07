package com.teikk.datn.data.datasource.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teikk.datn.data.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM category_tables")
    suspend fun getAllCategory(): List<Category>

}