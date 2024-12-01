package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.teikk.datn.utils.DateTimeConstant
import java.io.Serializable

@Entity(tableName = "order_tables")
data class Order(
    @PrimaryKey
    @SerializedName("_id")
    var id: String = "",
    @SerializedName("created_at")
    var createdAt: String = DateTimeConstant.getCurrentTimestampStr(),
    var status: String = "Pending",
    var total: Int = 0,
    var lat: Double = 0.0,
    var long: Double = 0.0,
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("payment_id")
    var paymentId: String = "",
    @SerializedName("is_shipment")
    var isShipment: Boolean = true,
    var description: String = ""
) : Serializable
