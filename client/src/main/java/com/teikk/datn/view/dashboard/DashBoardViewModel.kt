package com.teikk.datn.view.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.UploadFileRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.data.service.socket.SocketManager
import com.teikk.datn.utils.Resource
import com.teikk.datn.utils.ShareConstant
import com.teikk.datn.utils.ShareConstant.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val socket: SocketManager,
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    private val userProfileRepository: UserProfileRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val uploadFileRepository: UploadFileRepository
) : ViewModel(){
    private val _paymentMethod = paymentMethodRepository.paymentMethodsLiveData
    val paymentMethod get() = _paymentMethod
    private val _category = categoryRepository.categoriesLiveData
    val category get() = _category
    private val _user = MutableLiveData<Resource<UserProfile>>()
    val user get() = _user
    private val uid: String by lazy {
        sharedPreferenceUtils.getStringValue(UID, "")
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            paymentMethodRepository.fetchPaymentMethodData()
            _user.postValue(Resource.Success(userProfileRepository.getUserProfileByID(uid)))
        }
        socket.socketConnect()
    }

    fun connectSocket() {
        socket.socketConnect()
    }

    fun logout(logoutSuccess : () -> Unit)  {
        ShareConstant.removeToken(sharedPreferenceUtils)
        sharedPreferenceUtils.putStringValue(UID, "")
        viewModelScope.launch(Dispatchers.IO) {
            _user.value?.let { it.data?.let { it1 -> userProfileRepository.deleteUserFromLocal(it1) } }
        }
        logoutSuccess()
    }

    fun updateUser(user: UserProfile) = viewModelScope.launch(Dispatchers.IO) {
        _user.postValue(Resource.Loading())
        try {
            val responseRemote = async {  userProfileRepository.saveUserToRemote(user) }
            val responseLocal = async {  userProfileRepository.saveUserToLocal(user) }
            awaitAll(responseRemote, responseLocal)
            _user.postValue(Resource.Success(user))
        } catch (e: Exception) {
            _user.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun uploadFile(file: File, callback: (String?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = uploadFileRepository.uploadFile(file)
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }
}