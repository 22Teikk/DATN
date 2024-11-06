package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "order_tables")
data class Order(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    @SerializedName("created_at")
    var createdAt: String,
    var status: String,
    var total: Int,
    var lat: Double?,
    var long: Double?,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("payment_id")
    var paymentId: String,
    @SerializedName("is_shipment")
    var isShipment: Boolean
)
