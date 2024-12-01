package com.teikk.datn.data.datasource.repository

import android.util.Log
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
    init {
        fetchOrderItemLocal()
    }
    fun fetchOrderItemLocal() = CoroutineScope(Dispatchers.IO).launch {
        orderItemLocalRepository.getAllOrderItems().collect {
        }
    }

    suspend fun insertOrderItemRemote(orderItem: OrderItem) = orderItemRemoteRepository.createOrderItem(orderItem)

    fun insertOrderItemLocal(orderItem: OrderItem) = CoroutineScope(Dispatchers.IO).launch {
        orderItemLocalRepository.insertOrderItems(listOf(orderItem))
        fetchOrderItemLocal()
    }

    suspend fun fetchOrderItemRemote(orderID: String) =
        orderItemRemoteRepository.getOrderItemForOrder(orderID)

    fun deleteALllOrderItems() = CoroutineScope(Dispatchers.IO).launch {
        orderItemLocalRepository.deleteAllOrderItems()
    }

}