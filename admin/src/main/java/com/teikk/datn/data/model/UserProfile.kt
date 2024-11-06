package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.teikk.datn.utils.DateTimeConstant
import java.util.Date

@Entity(tableName = "user_table")
data class UserProfile(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("_id")
    var id: String = "",
    var address: String = "",
    @SerializedName("created_at")
    var createdAt: String = DateTimeConstant.getCurrentTimestampStr(),
    var description: String = "",
    var email: String = "",
    @SerializedName("image_url")
    var imageUrl: String? = null,
    var lat: Double? = null,
    var long: Double? = null,
    var name: String = "",
    var password: String = "",
    var phone: String = "",
    @SerializedName("role_id")
    var roleId: String = "e3df9e4c-f05e-4848-be4c-78f9dd4945d1",
    @SerializedName("store_id")
    var storeId: String? = null,
    var username: String = ""
)