package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.service.dao.WishlistDao
import com.teikk.datn.data.model.Wishlist
import javax.inject.Inject

class WishlistLocalRepository @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    suspend fun getAllWishlists() = wishlistDao.getAllWishlist()
    suspend fun insertWishlists(wishlists: List<Wishlist>) = wishlistDao.insertWishlists(wishlists)
    suspend fun deleteWishlist(wishlist: Wishlist) = wishlistDao.delete(wishlist)
}