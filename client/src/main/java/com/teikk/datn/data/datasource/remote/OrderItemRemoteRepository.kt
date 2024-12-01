package com.teikk.datn.data.datasource.remote

import com.teikk.datn.data.model.OrderItem
import com.teikk.datn.data.service.ApiService
import javax.inject.Inject

class OrderItemRemoteRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getOrderItemForOrder(orderId: String) = apiService.getOrderItemForOrder(orderId)
    suspend fun createOrderItem(order: OrderItem) = apiService.createOrderItem(order)
}