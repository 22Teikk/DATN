package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.service.dao.OrderDao
import com.teikk.datn.data.model.Order
import javax.inject.Inject

class OrderLocalRepository @Inject constructor(
    private val orderDao: OrderDao
) {
    suspend fun getAllOrders() = orderDao.getAllOrder()
    suspend fun insertOrders(orders: List<Order>) = orderDao.insertOrder(orders)
    suspend fun deleteOrder(order: Order) = orderDao.delete(order)
    suspend fun deleteAllOrders() = orderDao.deleteAllOrders()
}