package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.service.dao.OrderItemDao
import com.teikk.datn.data.model.OrderItem
import javax.inject.Inject

class OrderItemLocalRepository @Inject constructor(
    private val orderItemDao: OrderItemDao
) {
    suspend fun getAllOrderItems() = orderItemDao.getAllOrderItem()
    suspend fun insertOrderItems(orderItems: List<OrderItem>) = orderItemDao.insertOrderItem(orderItems)
    suspend fun deleteOrderItem(orderItem: OrderItem) = orderItemDao.delete(orderItem)
    suspend fun deleteAllOrderItems() = orderItemDao.deleteAllOrderItems()
}