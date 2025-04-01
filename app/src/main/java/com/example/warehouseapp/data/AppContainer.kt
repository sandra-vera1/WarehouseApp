package com.example.warehouseapp.data

import android.content.Context
import com.example.warehouseapp.data.database.WarehouseDatabase
import com.example.warehouseapp.data.repositories.GoodsRepository
import com.example.warehouseapp.data.repositories.OfflineGoodsRepository
import com.example.warehouseapp.data.repositories.OfflineUserRepository
import com.example.warehouseapp.data.repositories.OfflineWarehouseRepository
import com.example.warehouseapp.data.repositories.UserRepository
import com.example.warehouseapp.data.repositories.WarehouseRepository

interface AppContainer {
    val warehouseRepository: WarehouseRepository
    val goodsRepository: GoodsRepository
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val warehouseRepository: WarehouseRepository by lazy {
        OfflineWarehouseRepository(WarehouseDatabase.getDatabase(context).warehouseDao())
    }
    override val goodsRepository: GoodsRepository by lazy {
        OfflineGoodsRepository(WarehouseDatabase.getDatabase(context).goodsDao())
    }
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(WarehouseDatabase.getDatabase(context).userDao())
    }
}