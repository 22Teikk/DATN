package com.teikk.datn.data.datasource.remote

import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.service.ApiService
import com.teikk.datn.data.model.UserProfile
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferenceUtils: SharedPreferenceUtils
) {
    suspend fun login(userProfile: UserProfile) = apiService.login(userProfile)
    suspend fun register(userProfile: UserProfile) = apiService.register(userProfile)
}