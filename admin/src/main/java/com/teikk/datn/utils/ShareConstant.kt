package com.teikk.datn.utils

import com.teikk.datn.base.SharedPreferenceUtils

object ShareConstant {
    private const val TOKEN = "TOKEN"

    fun saveToken(sharedPreferenceUtils: SharedPreferenceUtils, token: String) {
        sharedPreferenceUtils.putStringValue(TOKEN, token)
    }

    fun removeToken(sharedPreferenceUtils: SharedPreferenceUtils) =
        sharedPreferenceUtils.putStringValue(
            TOKEN, ""
        )

    fun getAdminHeaders(sharedPreferenceUtils: SharedPreferenceUtils) = mapOf(
        "Authorization" to "Bearer ${sharedPreferenceUtils.getStringValue(TOKEN, "")}",
        "Authentication-Admin" to "teikk"
    )

    fun getHeaders(sharedPreferenceUtils: SharedPreferenceUtils) = mapOf(
        "Authorization" to "Bearer ${sharedPreferenceUtils.getStringValue(TOKEN, "")}"
    )
}