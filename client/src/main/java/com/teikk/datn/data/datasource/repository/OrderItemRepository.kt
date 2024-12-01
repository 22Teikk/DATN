package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.datasource.local.OrderItemLocalRepository
import com.teikk.datn.data.datasource.remote.OrderItemRemoteRepository
import com.teikk.datn.data.model.OrderItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderItemRepository @Inject constructor(
    private val orderItemRemoteRepository: OrderItemRemoteRepository,
    private val orderItemLocalRepository: OrderItemLocalRepository,
) {
    private val _orderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val orderItems get()= _orderItems
    init {
        fetchOrderItemLocal()
    }
    fun fetchOrderItemLocal() = CoroutineScope(Dispatchers.IO).launch {
        orderItemLocalRepository.getAllOrderItems().collect {
            _orderItems.value = it
        }
    }

    suspend fun insertOrderItemRemote(orderItem: OrderItem) = orderItemRemoteRepository.createOrderItem(orderItem)

    fun insertOrderItemLocal(orderItem: OrderItem) = CoroutineScope(Dispatchers.IO).launch {
        orderItemLocalRepository.insertOrderItems(listOf(orderItem))
        fetchOrderItemLocal()
    }

    fun fetchOrderItemRemote(orderID: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = orderItemRemoteRepository.getOrderItemForOrder(orderID)
        if (response.isSuccessful) {
            val orderItems = response.body()!!
            orderItemLocalRepository.insertOrderItems(orderItems)
        }
    }

    fun deleteALllOrderItems() = CoroutineScope(Dispatchers.IO).launch {
        orderItemLocalRepository.deleteAllOrderItems()
    }

}