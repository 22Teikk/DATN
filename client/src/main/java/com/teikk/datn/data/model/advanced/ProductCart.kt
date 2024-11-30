package com.teikk.datn.data.model.advanced

import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.model.Product

data class ProductCart(
    var product: Product,
    var cart: Cart,
)
