package com.teikk.datn.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.teikk.datn.utils.DateTimeConstant

@Entity(tableName = "wishlist_tables")
data class Wishlist(
    @SerializedName("_id")
    var id: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("product_id")
    var productId: String,
    @SerializedName("created_at")
    var createdAt: String = DateTimeConstant.getCurrentTimestampStr()
)