package com.teikk.datn.data.datasource.remote

import com.teikk.datn.data.datasource.service.ApiService
import javax.inject.Inject

class EntityRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getAllEntities(headers: Map<String, String>) = apiService.getAllEntities(headers)
}