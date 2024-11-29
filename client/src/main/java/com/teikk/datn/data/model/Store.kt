package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "store_tables")
data class Store(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var name: String,
    var address: String,
    var description: String,
    var lat: Double? = null,
    var long: Double? = null,
    @SerializedName("open_time")
    var openTime: String,
    @SerializedName("close_time")
    var closeTime: String,
    @SerializedName("image_src")
    var imageUrl: String,
    var phone: String,
    @SerializedName("open_day")
    var openDay: String,
    var email: String,
)
