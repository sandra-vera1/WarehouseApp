package com.example.warehouseapp.data

data class User(
    var id: Int,
    var userName: String,
    var password: String,
    var role: Int,
)

const val ROLE_ADMIN = 1;

val userList = listOf(
    User(
        id = 1,
        userName = "svera",
        password = "1",
        role = ROLE_ADMIN
    ),
    User(
        id = 2,
        userName = "erick",
        password = "1",
        role = ROLE_ADMIN
    ),
    User(
        id = 3,
        userName = "emplo1",
        password = "1",
        role = 2
    ),
    User(
        id = 4,
        userName = "emplo2",
        password = "1",
        role = 2
    )
)