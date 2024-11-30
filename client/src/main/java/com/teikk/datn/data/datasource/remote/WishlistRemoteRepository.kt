package com.teikk.datn.data.datasource.remote

import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.data.service.ApiService
import javax.inject.Inject

class WishlistRemoteRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getWishlistForUser(uid: String) = apiService.getWishlistForUser(uid)
    suspend fun addToWishlist(wishlist: Wishlist) = apiService.addToWishlist(wishlist)
    suspend fun deleteFromList(id: String) = apiService.deleteFromWishlist(id)
}