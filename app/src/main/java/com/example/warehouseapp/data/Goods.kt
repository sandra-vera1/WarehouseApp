package com.example.warehouseapp.data

data class Goods(
    var id: Int,
    var name: String,
    var description: String,
    var quantity: Int,
    var image: String,
    var warehouseId : Int?
)

val goodsList = listOf(
    Goods(
        id = 1,
        name = "Pencil",
        description = "Black Pencil",
        quantity= 250,
        image= "default.png",
        warehouseId = 1
    ),
    Goods(
        id = 2,
        name = "Pen",
        description = "Black Pen",
        quantity= 350,
        image= "default.png",
        warehouseId = 1
    ),
    Goods(
        id = 3,
        name = "Eraser",
        description = "Paper eraser",
        quantity= 120,
        image= "default.png",
        warehouseId = 1
    ),
    Goods(
        id = 4,
        name = "Box",
        description = "Square box",
        quantity= 800,
        image= "default.png",
        warehouseId = 2
    ),
    Goods(
        id = 5,
        name = "Table",
        description = "Rainbow table",
        quantity= 300,
        image= "default.png",
        warehouseId = 2
    )
)