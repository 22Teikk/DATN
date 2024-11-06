package com.teikk.datn.view.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.remote.AuthRepository
import com.teikk.datn.data.datasource.remote.AuthenticationRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.utils.ShareConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val authRepository: AuthRepository,
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    private val roleRemoteRepository: RoleRemoteRepository,
    private val roleLocalRepository: RoleLocalRepository
) : ViewModel() {
    private val TAG = "AuthenticationViewModel-TAG"

    init {
        fetchRoleData()
    }

    fun fetchRoleData() = viewModelScope.launch {
        val response = roleRemoteRepository.getAllRoles()
        if (response.isSuccessful) {
            val roles = response.body()!!
            Log.d(TAG, roles.toString())
            roleLocalRepository.insertRoles(roles)
        }
    }

    fun signUpWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        authenticationRepository.signUpWithEmail(email, password) {
            callback(it)
        }
    }

    fun signInWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        authenticationRepository.signInWithEmail(email, password) {
            callback(it)
        }
    }

    fun register(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        val userProfile = UserProfile(password = password, username = email, name = email, email = email, lat = 0.0, long = 0.0)
        val response = authRepository.register(userProfile)
        if (response.isSuccessful) {
            val jsonObject = response.body()
            Log.d(TAG, jsonObject.toString())
            val data = jsonObject?.get("message")?.asString
            Log.d(TAG, data.toString())
        } else {
            Log.d(TAG, response.message())
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        val userProfile = UserProfile(password = password, username = email)
        val response = authRepository.login(userProfile)
        if (response.isSuccessful) {
            val jsonObject = response.body()
            val userProfile = Gson().fromJson(jsonObject?.getAsJsonObject("data"), UserProfile::class.java)
            val tokens = jsonObject?.getAsJsonObject("tokens")
            val accessToken = tokens?.get("access")?.asString
            val refreshToken = tokens?.get("refresh")?.asString
            ShareConstant.saveToken(sharedPreferenceUtils, accessToken!!)
        } else {
            Log.d(TAG, response.message())
        }
    }
}