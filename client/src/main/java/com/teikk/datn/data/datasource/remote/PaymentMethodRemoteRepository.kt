package com.teikk.datn.data.datasource.remote

import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.service.ApiService
import com.teikk.datn.utils.ShareConstant
import javax.inject.Inject

class PaymentMethodRemoteRepository @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferenceUtils: SharedPreferenceUtils
) {
    suspend fun getAllPaymentMethods() = apiService.getAllPaymentMethods(ShareConstant.getAdminHeaders(sharedPreferenceUtils))
}