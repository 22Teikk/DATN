package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.datasource.local.OrderLocalRepository
import com.teikk.datn.data.datasource.remote.OrderRemoteRepository
import com.teikk.datn.data.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderRemoteRepository: OrderRemoteRepository,
    private val orderLocalRepository: OrderLocalRepository,
) {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders get()= _orders
    init {
        fetchOrderLocal()
    }
    fun fetchOrderLocal() = CoroutineScope(Dispatchers.IO).launch {
        orderLocalRepository.getAllOrders().collect {
            _orders.value = it
        }
    }

    suspend fun insertOrderRemote(order: Order) = orderRemoteRepository.createOrder(order)
    suspend fun updateOrder(order: Order) = orderRemoteRepository.updateOrder(order)

    fun insertOrderLocal(order: Order) = CoroutineScope(Dispatchers.IO).launch {
        orderLocalRepository.insertOrders(listOf(order))
        fetchOrderLocal()
    }

    fun fetchOrderRemote(uid: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = orderRemoteRepository.getOrderForUser(uid)
        if (response.isSuccessful) {
            val orders = response.body()!!
            orderLocalRepository.insertOrders(orders)
        }
    }

    fun deleteALllOrders() = CoroutineScope(Dispatchers.IO).launch {
        orderLocalRepository.deleteAllOrders()
    }

}