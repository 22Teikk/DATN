package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "order_item_tables")
data class OrderItem(
    @PrimaryKey
    @SerializedName("_id")
    var id: String = "",
    var price: Double = 0.0,
    var quantity: Int = 1,
    @SerializedName("order_id")
    var orderId: String = "",
    @SerializedName("product_id")
    var productId: String = "",
)
