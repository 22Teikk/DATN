package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "discount_tables")
data class Discount(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var code: String,
    @SerializedName("discount_percent")
    var discountPercent: Double,
    @SerializedName("start_date")
    var startDate: String,
    @SerializedName("end_date")
    var endDate: String,
    @SerializedName("is_active")
    var isActive: Boolean,
    var total: Int
)
