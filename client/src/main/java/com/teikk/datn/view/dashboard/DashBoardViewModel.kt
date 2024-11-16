package com.teikk.datn.view.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.data.service.socket.SocketManager
import com.teikk.datn.utils.ShareConstant
import com.teikk.datn.utils.ShareConstant.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val socket: SocketManager,
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    private val userProfileRepository: UserProfileRepository
) : ViewModel(){
    private val _category = categoryRepository.categoriesLiveData
    val category get() = _category
    private val _user = MutableLiveData<UserProfile>()
    val user get() = _user
    private val uid: String by lazy {
        sharedPreferenceUtils.getStringValue(UID, "")
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userProfileRepository.getUserProfileByID(uid))
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
            _user.value?.let { userProfileRepository.deleteUserFromLocal(it) }
        }
        logoutSuccess()
    }
}