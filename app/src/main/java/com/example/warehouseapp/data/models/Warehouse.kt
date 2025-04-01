package com.example.warehouseapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warehouse")
data class Warehouse(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var province: String,
    var locationAddress: String,
    var totalGoods: Int
)