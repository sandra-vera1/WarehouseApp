package com.example.warehouseapp.util.repository

import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.data.repositories.WarehouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWarehouseRepository : WarehouseRepository {
    override fun getAllWarehousesStream(): Flow<List<Warehouse>> {
        return flowOf()
    }

    override fun getWarehouseStream(id: Int): Flow<Warehouse?> {
        return flowOf(null)
    }

    override suspend fun insertWarehouse(warehouse: Warehouse) {
        // no-op
    }

    override suspend fun deleteWarehouse(warehouse: Warehouse) {
        // no-op
    }

    override suspend fun updateWarehouse(warehouse: Warehouse) {
        // no-op
    }

}