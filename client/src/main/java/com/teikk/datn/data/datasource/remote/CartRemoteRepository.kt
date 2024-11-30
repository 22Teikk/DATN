package com.teikk.datn.data.datasource.remote

import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.service.ApiService
import javax.inject.Inject

class CartRemoteRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getCartForUser(uid: String) = apiService.getCartForUser(uid)
    suspend fun addToCart(wishlist: Cart) = apiService.addToCart(wishlist)
    suspend fun deleteFromList(id: String) = apiService.deleteFromCart(id)
    suspend fun updateCart(cart: Cart) = apiService.updateCart(cart.id, cart)
    suspend fun updateManyCarts(carts: List<Cart>) = apiService.updateManyCarts(carts)
}