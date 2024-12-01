package com.teikk.datn.data.datasource.remote

import com.teikk.datn.data.model.Order
import com.teikk.datn.data.service.ApiService
import javax.inject.Inject

class OrderRemoteRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getOrderForUser(uid: String) = apiService.getOrderForUser(uid)
    suspend fun createOrder(order: Order) = apiService.createOrder(order)
}