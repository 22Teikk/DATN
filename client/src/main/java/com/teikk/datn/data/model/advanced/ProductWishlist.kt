package com.teikk.datn.data.model.advanced

import com.teikk.datn.data.model.Product
import com.teikk.datn.data.model.Wishlist

data class ProductWishlist(
    var product: Product,
    var wishlist: Wishlist?,
)
