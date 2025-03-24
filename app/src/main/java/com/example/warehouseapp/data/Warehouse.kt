package com.example.warehouseapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import com.example.warehouseapp.R

data class Warehouse(
    val id: Int,
    @StringRes val name: Int,
    @StringRes val province: Int,
    @StringRes val locationAddress: Int
)

val warehouses = listOf(
    Warehouse(
        id = 1,
        name = R.string.warehouse_name_1,
        province = R.string.province_1,
        locationAddress = R.string.location_1
    ),
    Warehouse(
        id = 2,
        name = R.string.warehouse_name_2,
        province = R.string.province_2,
        locationAddress = R.string.location_2
    )
)