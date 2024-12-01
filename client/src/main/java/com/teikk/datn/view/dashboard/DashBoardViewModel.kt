package com.teikk.datn.view.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.FuelPrice
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.repository.CartRepository
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.OrderRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.ProductRepository
import com.teikk.datn.data.datasource.repository.SummaryRepository
import com.teikk.datn.data.datasource.repository.UploadFileRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import com.teikk.datn.data.datasource.repository.WishListRepository
import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.model.Order
import com.teikk.datn.data.model.Payment
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.data.service.socket.SocketManager
import com.teikk.datn.utils.DateTimeConstant
import com.teikk.datn.utils.Resource
import com.teikk.datn.utils.ShareConstant
import com.teikk.datn.utils.ShareConstant.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
    private val wishListRepository: WishListRepository,
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val summaryRepository: SummaryRepository
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
    private val _carts = cartRepository.carts
    val carts get() = _carts
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
        fetchWishlistData()
        fetchOrderData()
        fetchCartData()
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
            val deleteUserTask = async {
                _user.value?.data?.let { userProfileRepository.deleteUserFromLocal(it) }
            }
            val deleteAllUsersTask = async { userProfileRepository.deleteAllUser() }
            val deleteAllWishlistsTask = async { wishListRepository.deleteALllWishlists() }
            val deleteAllCartsTask = async { cartRepository.deleteALllCarts() }
            val deleteOrderTask = async { orderRepository.deleteALllOrders()}
            // Chờ tất cả các task hoàn thành
            deleteUserTask.await()
            deleteAllUsersTask.await()
            deleteAllWishlistsTask.await()
            deleteAllCartsTask.await()
            deleteOrderTask.await()
            logoutSuccess()
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


    // FetchData
    fun fetchProductData() = viewModelScope.launch(Dispatchers.IO) {
        productRepository.fetchProductRemote()
        productRepository.fetchProductLocal()
    }

    fun fetchWishlistData() = viewModelScope.launch(Dispatchers.IO) {
        wishListRepository.fetchWishlistRemote(uid)
    }
    fun fetchCartData() = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.fetchCartRemote(uid)
    }

    fun fetchOrderData() = viewModelScope.launch(Dispatchers.IO) {
        orderRepository.fetchOrderRemote(uid)
    }

    // FetchData
    fun uploadFile(file: File, callback: (String?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = uploadFileRepository.uploadFile(file)
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }

    // Wishlist
    fun insertWishlist(wishlist: Wishlist) = viewModelScope.launch(Dispatchers.IO) {
        wishListRepository.insertWishlist(wishlist)
    }

    fun deleteWishlist(wishlist: Wishlist) = viewModelScope.launch(Dispatchers.IO) {
        wishListRepository.deleteWishlist(wishlist)
    }
    // Wishlist

    // Cart
    fun insertCart(cart: Cart) = viewModelScope.launch(Dispatchers.IO) {
        val existingCart = _carts.value.find { it.productId == cart.productId }
        if (existingCart != null) {
            val updatedCart = existingCart.copy(quantity = existingCart.quantity + cart.quantity)
            Log.d("CART_DATA", "Update: " + updatedCart.toString())
            cartRepository.updateCart(updatedCart)
        } else {
            Log.d("CART_DATA", "Insert New Cart: " + cart.toString())
            cartRepository.insertCart(cart)
        }
    }

    fun deleteCart(cart: Cart) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.deleteCart(cart)
    }

    fun updateManyCarts(carts: List<Cart>) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.updateManyCarts(carts)
    }
    // Cart

    // Order
    fun createOrder(order: Order, price: Double) = viewModelScope.launch(Dispatchers.IO) {
        val payment = Payment(
            id = "",
            amount = price,
            paymentMethodID = "1",
            createdAt = DateTimeConstant.getCurrentTimestampStr(),
        )
        val createPayment = summaryRepository.makePayment(payment)
        if (createPayment.isSuccessful) {
            val newPayment = payment.copy(id = createPayment.body()!!.id)
            order.paymentId = newPayment.id
            val response =  orderRepository.insertOrderRemote(order)
            if (response.isSuccessful) {
                val newOrder = order.copy(
                    id = response.body()!!.id,
                )
                orderRepository.insertOrderLocal(newOrder)
                cartRepository.deleteALllCarts()
            }
        }
    }
    // Order

}