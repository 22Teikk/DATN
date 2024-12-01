package com.teikk.datn.data.model.advanced

import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.model.OrderItem
import com.teikk.datn.data.model.Product

data class ProductOrderItem(
    var product: Product,
    var orderItem: OrderItem,
)
