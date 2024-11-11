package com.teikk.datn.view.splash

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.RoleRepository
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
    init {
        val firstTime = sharedPreferences.getBooleanValue("isFirstTime", true)
        if (firstTime) {
            firstInit()
        } else {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                _isFirstTime.value = false
            }, 2000)
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