package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.service.dao.CartDao
import com.teikk.datn.data.model.Cart
import javax.inject.Inject

class CartLocalRepository @Inject constructor(
    private val cartDao: CartDao
) {
    suspend fun getAllCarts() = cartDao.getAllCart()
    suspend fun insertCarts(carts: List<Cart>) = cartDao.insertCarts(carts)
    suspend fun deleteCart(cart: Cart) = cartDao.delete(cart)
    suspend fun deleteAllCarts() = cartDao.deleteAllCarts()
}