package com.teikk.datn.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category_tables")
data class Category(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var name: String,
    @SerializedName("image_url")
    @ColumnInfo(defaultValue =  "")
    var imageUrl: String
)
