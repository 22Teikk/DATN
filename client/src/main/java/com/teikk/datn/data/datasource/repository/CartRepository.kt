package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.datasource.local.CartLocalRepository
import com.teikk.datn.data.datasource.remote.CartRemoteRepository
import com.teikk.datn.data.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartRemoteRepository: CartRemoteRepository,
    private val cartLocalRepository: CartLocalRepository,
) {
    private val _cart = MutableStateFlow<List<Cart>>(emptyList())
    val carts get()= _cart
    init {
        fetchCartLocal()
    }
    fun fetchCartLocal() = CoroutineScope(Dispatchers.IO).launch {
        cartLocalRepository.getAllCarts().collect {
            _cart.value = it
        }
    }

    fun insertCart(cart: Cart) = CoroutineScope(Dispatchers.IO).launch {
        val response = cartRemoteRepository.addToCart(cart)
        if (response.isSuccessful) {
            val cart = response.body()!!.apply {
                userId = cart.userId
                productId = cart.productId
                createdAt = cart.createdAt
                quantity = cart.quantity
            }
            cartLocalRepository.insertCarts(listOf(cart))
            fetchCartLocal()
        }
    }

    fun deleteCart(cart: Cart) = CoroutineScope(Dispatchers.IO).launch {
        cartLocalRepository.deleteCart(cart)
        val response = cartRemoteRepository.deleteFromList(cart.id)
        if (response.isSuccessful) {
            fetchCartLocal()
        }
    }

    fun fetchCartRemote(uid: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = cartRemoteRepository.getCartForUser(uid)
        if (response.isSuccessful) {
            val carts = response.body()!!
            cartLocalRepository.insertCarts(carts)
        }
    }

    fun deleteALllCarts() = CoroutineScope(Dispatchers.IO).launch {
        cartLocalRepository.deleteAllCarts()
    }

    fun updateCart(cart: Cart) = CoroutineScope(Dispatchers.IO).launch {
        cartLocalRepository.insertCarts(listOf(cart))
        cartRemoteRepository.updateCart(cart)
    }
}