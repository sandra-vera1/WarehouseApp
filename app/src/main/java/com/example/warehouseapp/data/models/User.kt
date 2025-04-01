package com.example.warehouseapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var userName: String,
    var password: String,
    var role: Int,
)