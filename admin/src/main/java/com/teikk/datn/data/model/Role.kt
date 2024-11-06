package com.teikk.datn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "role_tables")
data class Role(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("_id")
    var id: String = "",
    var name: String,
)