package com.example.warehouseapp.data

data class Warehouse(
    var id: Int,
    var name: String,
    var province: String,
    var locationAddress: String,
    var totalGoods: Int
)

val warehousesList = listOf(
    Warehouse(
        1,
        "Main Warehouse",
        "ON",
        "123 Main Street",
        720
    ),
    Warehouse(
        2,
        "Backup Warehouse",
        "BC",
        "456 SecondAvenue",
        1100
    )
)