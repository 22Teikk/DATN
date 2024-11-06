package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.teikk.datn.utils.DateTimeConstant

@Entity(tableName = "feedback_tables")
data class Feedback(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var star: Int,
    @SerializedName("created_at")
    var createdAt: String = DateTimeConstant.getCurrentTimestampStr(),
    var title: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("product_id")
    var productId: String
)
