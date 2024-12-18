package com.teikk.datn.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.MyApp
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CartRepository
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.OrderRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.RoleRepository
import com.teikk.datn.data.datasource.repository.SummaryRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.datasource.repository.WishListRepository
import com.teikk.datn.data.model.UserProfile
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
    private val paymentMethodRepository: PaymentMethodRepository,
    private val summaryRepository: SummaryRepository,
    private val wishListRepository: WishListRepository,
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {
    private val TAG = "SplashViewModel-TAG"
    private val _isFirstTime = MutableLiveData<Boolean>()
    val isFirstTime get() = _isFirstTime

    val uid = sharedPreferences.getStringValue(UID, "")
    init {
        val firstTime = sharedPreferences.getBooleanValue("isFirstTime", true)
        fetchData()
        if (firstTime) {
            firstInit()
        } else {
            _isFirstTime.value = false
        }
    }

    private fun firstInit() = viewModelScope.launch(Dispatchers.IO) {
        roleRepository.fetchRoleData()
        paymentMethodRepository.fetchPaymentMethodData()
        _isFirstTime.postValue(true)
    }

    private fun fetchData() = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.fetchCategoryData()
        val response = summaryRepository.fetchDataStore()
        if (response.isSuccessful) {
            MyApp.store = response.body()!!
        }
        if (uid != "") {
            wishListRepository.fetchWishlistRemote(uid)
            cartRepository.fetchCartRemote(uid)
            orderRepository.fetchOrderRemote(uid)
            userProfileRepository.getUserProfileRemote(uid)
        }
    }
}