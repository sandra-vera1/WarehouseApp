package com.example.warehouseapp.data.repositories

import com.example.warehouseapp.data.models.Warehouse
import kotlinx.coroutines.flow.Flow

interface WarehouseRepository {
    /**
     * Retrieve all the warehouses from the the given data source.
     */
    fun getAllWarehousesStream(): Flow<List<Warehouse>>

    /**
     * Retrieve an warehouse from the given data source that matches with the [id].
     */
    fun getWarehouseStream(id: Int): Flow<Warehouse?>

    /**
     * Insert warehouse in the data source
     */
    suspend fun insertWarehouse(warehouse: Warehouse)

    /**
     * Delete warehouse from the data source
     */
    suspend fun deleteWarehouse(warehouse: Warehouse)

    /**
     * Update warehouse in the data source
     */
    suspend fun updateWarehouse(warehouse: Warehouse)
}