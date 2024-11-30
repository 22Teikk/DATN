package com.teikk.datn.data.datasource.repository

import android.util.Log
import com.teikk.datn.data.datasource.local.WishlistLocalRepository
import com.teikk.datn.data.datasource.remote.WishlistRemoteRepository
import com.teikk.datn.data.model.Wishlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishListRepository @Inject constructor(
    private val wishlistRemoteRepository: WishlistRemoteRepository,
    private val wishlistLocalRepository: WishlistLocalRepository,
) {
    private val _wishlist = MutableStateFlow<List<Wishlist>>(emptyList())
    val wishlists get()= _wishlist
    init {
        fetchWishlistLocal()
    }
    fun fetchWishlistLocal() = CoroutineScope(Dispatchers.IO).launch {
        wishlistLocalRepository.getAllWishlists().collect {
            _wishlist.value = it
        }
    }

    fun insertWishlist(wishlist: Wishlist) = CoroutineScope(Dispatchers.IO).launch {
        val response = wishlistRemoteRepository.addToWishlist(wishlist)
        if (response.isSuccessful) {
            val wishlist = response.body()!!.apply {
                userId = wishlist.userId
                productId = wishlist.productId
                createdAt = wishlist.createdAt
            }
            wishlistLocalRepository.insertWishlists(listOf(wishlist))
            fetchWishlistLocal()
        }
    }

    fun deleteWishlist(wishlist: Wishlist) = CoroutineScope(Dispatchers.IO).launch {
        wishlistLocalRepository.deleteWishlist(wishlist)
        val response = wishlistRemoteRepository.deleteFromList(wishlist.id)
        if (response.isSuccessful) {
            fetchWishlistLocal()
        }
    }

    fun fetchWishlistRemote(uid: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = wishlistRemoteRepository.getWishlistForUser(uid)
        if (response.isSuccessful) {
            val wishlists = response.body()!!
            wishlistLocalRepository.insertWishlists(wishlists)
        }
    }
}