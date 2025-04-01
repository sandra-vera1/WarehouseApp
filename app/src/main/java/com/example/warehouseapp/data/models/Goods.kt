package com.example.warehouseapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class Goods(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var description: String,
    var quantity: Int,
    var image: String,
    var warehouseId : Int?
)