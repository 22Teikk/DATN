package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity("product_tables")
data class Product(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var name: String,
    var description: String,
    var price: Double,
    @SerializedName("quantity_sold")
    var quantity: Int,
    @SerializedName("is_sold")
    var isSold: Boolean,
    @SerializedName("total_time")
    var totalTime: Int,
    @SerializedName("category_id")
    var categoryId: String,
    var thumbnail: String,
    @SerializedName("discount_id")
    var discountId: String? = null,
) : Serializable
