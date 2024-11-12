package com.teikk.datn.view.dashboard

import androidx.lifecycle.ViewModel
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.service.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val socket: SocketManager,
) : ViewModel(){
    private val _category = categoryRepository.categoriesLiveData
    val category get() = _category
    init {
        socket.socketConnect()
    }

    fun connectSocket() {
        socket.socketConnect()
    }
}