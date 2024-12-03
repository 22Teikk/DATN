package com.teikk.datn.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.OrderRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.model.Order
import com.teikk.datn.data.model.advanced.EmployeeLocation
import com.teikk.datn.data.service.socket.SocketManager
import com.teikk.datn.utils.ShareConstant.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    val socketManager: SocketManager,
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    private val orderRepository: OrderRepository,
) : ViewModel(){
    val uid: String by lazy {
        sharedPreferenceUtils.getStringValue(UID, "")
    }

    init {
        connectSocket()
    }

    private fun connectSocket() {
        socketManager.socketConnect()
        socketManager.joinCustomer(uid)
    }

    private val _mapType = MutableLiveData<Int>(1)
    val mapType: LiveData<Int>
        get() = _mapType

    private val _employeeLocation = MutableLiveData<EmployeeLocation>()
    val employeeLocation get()= _employeeLocation

    fun changeTypeMap(mapType: Int) {
        viewModelScope.launch {
            _mapType.value = mapType
        }
    }

    fun updateOrder(order: Order, callback: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val response = orderRepository.updateOrder(order)
        if (response.isSuccessful) {
            orderRepository.fetchOrderRemote(uid)
        }
    }
}