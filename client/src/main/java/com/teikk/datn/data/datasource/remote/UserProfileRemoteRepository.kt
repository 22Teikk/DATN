package com.teikk.datn.data.datasource.remote

import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.service.ApiService
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.utils.ShareConstant
import javax.inject.Inject

class UserProfileRemoteRepository @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferenceUtils: SharedPreferenceUtils
) {
    suspend fun updateUserProfile(user: UserProfile) = apiService.updateUserProfile(ShareConstant.getAdminHeaders(sharedPreferenceUtils), user.id, user)
}