package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.datasource.service.dao.CategoryDao
import com.teikk.datn.data.model.Category
import javax.inject.Inject

class CategoryLocalRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend fun getAllCategories() = categoryDao.getAllCategory()
    suspend fun insertCategories(categories: List<Category>) = categoryDao.insertCategories(categories)
    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)
}