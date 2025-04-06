package com.example.warehouseapp.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "warehouse")
data class Warehouse(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var province: String,
    var locationAddress: String
) {
    @Ignore
    var totalGoods: Int = 0

    constructor(id: Int, name: String, province: String, locationAddress: String, totalGoods: Int) : this(id, name, province, locationAddress) {
        this.totalGoods = totalGoods
    }
}