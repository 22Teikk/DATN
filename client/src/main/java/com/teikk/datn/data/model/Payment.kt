package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.teikk.datn.utils.DateTimeConstant

@Entity(tableName = "payment_tables")
data class Payment(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var amount: Double,
    @SerializedName("created_at")
    var createdAt: String = DateTimeConstant.getCurrentTimestampStr(),
    @SerializedName("payment_method_id")
    var paymentMethodID: String
)
