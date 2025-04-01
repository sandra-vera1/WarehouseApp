package com.example.warehouseapp.data.repositories

import com.example.warehouseapp.data.database.WarehouseDao
import com.example.warehouseapp.data.models.Warehouse
import kotlinx.coroutines.flow.Flow

class OfflineWarehouseRepository (private val warehouseDao: WarehouseDao): WarehouseRepository{
    override fun getAllWarehousesStream(): Flow<List<Warehouse>> = warehouseDao.getAllWarehouses()

    override fun getWarehouseStream(id: Int): Flow<Warehouse?> = warehouseDao.getWarehouse(id)

    override suspend fun insertWarehouse(warehouse: Warehouse) = warehouseDao.insert(warehouse)

    override suspend fun deleteWarehouse(warehouse: Warehouse) = warehouseDao.delete(warehouse)

    override suspend fun updateWarehouse(warehouse: Warehouse) = warehouseDao.update(warehouse)
}