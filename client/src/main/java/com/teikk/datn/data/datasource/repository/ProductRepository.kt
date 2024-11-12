package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.datasource.local.ProductLocalRepository
import com.teikk.datn.data.datasource.remote.ProductRemoteRepository
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteRepository: ProductRemoteRepository,
    private val productLocalRepository: ProductLocalRepository,
) {
}