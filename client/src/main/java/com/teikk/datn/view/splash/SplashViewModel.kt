package com.teikk.datn.view.splash

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.RoleRepository
import com.teikk.datn.utils.ShareConstant.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferenceUtils,
    private val roleRepository: RoleRepository,
    private val categoryRepository: CategoryRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : ViewModel() {
    private val TAG = "SplashViewModel-TAG"
    private val _isFirstTime = MutableLiveData<Boolean>()
    val isFirstTime get() = _isFirstTime

    val uid = sharedPreferences.getStringValue(UID, "")
    init {
        val firstTime = sharedPreferences.getBooleanValue("isFirstTime", true)
        if (firstTime) {
            firstInit()
        } else {
            _isFirstTime.value = false
        }
    }

    private fun firstInit() = viewModelScope.launch(Dispatchers.IO) {
        roleRepository.fetchRoleData()
        categoryRepository.fetchCategoryData()
        paymentMethodRepository.fetchPaymentMethodData()
        sharedPreferences.putBooleanValue("isFirstTime", false)
        _isFirstTime.postValue(false)
    }
}