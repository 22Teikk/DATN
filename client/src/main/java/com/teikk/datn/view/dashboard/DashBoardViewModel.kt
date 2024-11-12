package com.teikk.datn.view.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel(){
    private val _category = categoryRepository.categoriesLiveData
    val category get() = _category
    init {

    }
}