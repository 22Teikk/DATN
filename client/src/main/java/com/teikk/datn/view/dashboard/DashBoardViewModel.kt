package com.teikk.datn.view.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.ProductRepository
import com.teikk.datn.data.datasource.repository.UploadFileRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.datasource.repository.WishListRepository
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.data.model.Wishlist
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
    val sharedPreferenceUtils: SharedPreferenceUtils,
    private val userProfileRepository: UserProfileRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val uploadFileRepository: UploadFileRepository,
    private val productRepository: ProductRepository,
    private val wishListRepository: WishListRepository
) : ViewModel(){
    private val _paymentMethod = paymentMethodRepository.paymentMethodsLiveData
    val paymentMethod get() = _paymentMethod
    private val _category = categoryRepository.categoriesLiveData
    val category get() = _category
    private val _user = MutableLiveData<Resource<UserProfile>>()
    val user get() = _user
    private val _products = productRepository.products
    val products get() = _products
    private val _wishlist = wishListRepository.wishlists
    val wishlist get() = _wishlist
    val uid: String by lazy {
        sharedPreferenceUtils.getStringValue(UID, "")
    }
    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch(Dispatchers.IO) {
            paymentMethodRepository.fetchPaymentMethodData()
            _user.postValue(Resource.Success(userProfileRepository.getUserProfileByID(uid)))
            wishListRepository.fetchWishlistRemote(uid)
        }
        fetchProductData()
        socket.socketConnect()
    }

    fun connectSocket() {
        socket.socketConnect()
    }

    // User

    fun logout(logoutSuccess : () -> Unit)  {
        ShareConstant.removeToken(sharedPreferenceUtils)
        sharedPreferenceUtils.putStringValue(UID, "")
        viewModelScope.launch(Dispatchers.IO) {
            _user.value?.let { it.data?.let { it1 -> userProfileRepository.deleteUserFromLocal(it1) } }
            userProfileRepository.deleteAllUser()
            wishListRepository.deleteALllWishlists()
            with(Dispatchers.Main) {
                logoutSuccess()
            }
        }
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

    //User


    // Product
    fun fetchProductData() = viewModelScope.launch(Dispatchers.IO) {
        productRepository.fetchProductRemote()
        productRepository.fetchProductLocal()
    }

    fun fetchWishlistData() = viewModelScope.launch(Dispatchers.IO) {
        wishListRepository.fetchWishlistRemote(uid)
    }

    // Product
    fun uploadFile(file: File, callback: (String?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = uploadFileRepository.uploadFile(file)
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }

    fun insertWishlist(wishlist: Wishlist) = viewModelScope.launch(Dispatchers.IO) {
        wishListRepository.insertWishlist(wishlist)
    }

    fun deleteWishlist(wishlist: Wishlist) = viewModelScope.launch(Dispatchers.IO) {
        wishListRepository.deleteWishlist(wishlist)
    }
}