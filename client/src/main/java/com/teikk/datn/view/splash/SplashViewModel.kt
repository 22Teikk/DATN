package com.teikk.datn.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.RoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferenceUtils,
    private val roleRepository: RoleRepository
) : ViewModel() {
    private val TAG = "SplashViewModel-TAG"
    private val _isFirstTime = MutableLiveData<Boolean>()
    private val rolesData = roleRepository.rolesLiveData
    init {
        _isFirstTime.value = sharedPreferences.getBooleanValue("isFirstTime", true)
        if (_isFirstTime.value == true) {
            firstInit()
        } else {
        }
    }

    private fun firstInit() = viewModelScope.launch(Dispatchers.IO) {
        roleRepository.fetchRoleData()
        sharedPreferences.putBooleanValue("isFirstTime", false)
        _isFirstTime.postValue(false)
    }


}