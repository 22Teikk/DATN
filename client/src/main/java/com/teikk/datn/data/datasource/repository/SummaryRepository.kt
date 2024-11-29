package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.service.ApiService
import javax.inject.Inject

class SummaryRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchDataStore() = apiService.getStoreByID()
}