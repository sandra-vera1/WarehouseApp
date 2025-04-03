package com.example.warehouseapp.data

data class Goods(
    var id: Int,
    var name: String,
    var description: String,
    var quantity: Int,
    var image: String,
    var warehouseId : Int?
)