package com.teikk.datn.data.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teikk.datn.data.datasource.local.CategoryLocalRepository
import com.teikk.datn.data.datasource.remote.CategoryRemoteRepository
import com.teikk.datn.data.model.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryRemoteRepository: CategoryRemoteRepository,
    private val categoryLocalRepository: CategoryLocalRepository,
){
    private val _categoriesLiveData = MutableLiveData<List<Category>>()
    val categoriesLiveData: LiveData<List<Category>> get() = _categoriesLiveData
    suspend fun fetchCategoryData()  {
        val response = categoryRemoteRepository.getAllCategories()
        if (response.isSuccessful) {
            val categories = response.body()!!
            categoryLocalRepository.insertCategories(categories)
            _categoriesLiveData.postValue(categories)
        }
    }
}