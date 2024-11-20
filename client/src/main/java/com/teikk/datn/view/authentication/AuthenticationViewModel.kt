package com.teikk.datn.view.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide.init
import com.google.gson.Gson
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.remote.AuthRepository
import com.teikk.datn.data.datasource.remote.AuthenticationRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.datasource.repository.UploadFileRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.utils.Resource
import com.teikk.datn.utils.ShareConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val authRepository: AuthRepository,
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    private val roleRemoteRepository: RoleRemoteRepository,
    private val roleLocalRepository: RoleLocalRepository,
    private val userProfileRepository: UserProfileRepository,
    private val uploadFileRepository: UploadFileRepository
) : ViewModel() {
    private val TAG = "AuthenticationViewModel-TAG"
    private val _user = MutableLiveData<Resource<UserProfile>>()
    val user get() = _user
    private val _userSuccess = MutableLiveData<Resource<UserProfile>>()
    val userSuccess get() = _userSuccess

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

    fun register(email: String, password: String, isSuccess: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val userProfile = UserProfile(password = password, username = email, name = email, email = email, lat = 0.0, long = 0.0)
        val response = authRepository.register(userProfile)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main) {
                isSuccess(true)
            }
        } else {
            withContext(Dispatchers.Main) {
                isSuccess(false)
                Log.d(TAG, response.message())
            }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _user.postValue(Resource.Loading())
        val userProfile = UserProfile(password = password, username = email, name = email)
        val response = authRepository.login(userProfile)
        if (response.isSuccessful) {
            val jsonObject = response.body()
            val userProfile = Gson().fromJson(jsonObject?.getAsJsonObject("data"), UserProfile::class.java)
            val tokens = jsonObject?.getAsJsonObject("tokens")
            val accessToken = tokens?.get("access")?.asString
            val refreshToken = tokens?.get("refresh")?.asString
            ShareConstant.saveToken(sharedPreferenceUtils, accessToken!!)
            sharedPreferenceUtils.putStringValue(ShareConstant.UID, userProfile.id)
            userProfileRepository.insertUserProfile(userProfile)
            _user.postValue(Resource.Success(userProfile))
        } else {
            _user.postValue(Resource.Error(response.message()))
            Log.d(TAG, response.message())
        }
    }

    fun updateUser(user: UserProfile) = viewModelScope.launch(Dispatchers.IO) {
        _userSuccess.postValue(Resource.Loading())
        try {
            val responseRemote = async {  userProfileRepository.saveUserToRemote(user) }
            val responseLocal = async {  userProfileRepository.insertUserProfile(user) }
            awaitAll(responseRemote, responseLocal)
            _userSuccess.postValue(Resource.Success(user))
        } catch (e: Exception) {
            _userSuccess.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun uploadFile(file: File, callback: (String?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = uploadFileRepository.uploadFile(file)
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }
}