package com.example.warehouseapp.data

data class Warehouse(
    var id: Int,
    var name: String,
    var province: String,
    var locationAddress: String,
    var totalGoods: Int
)