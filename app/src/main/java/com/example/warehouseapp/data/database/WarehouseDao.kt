package com.example.warehouseapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.warehouseapp.data.models.Warehouse
import kotlinx.coroutines.flow.Flow

@Dao
interface WarehouseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(warehouse: Warehouse)

    @Update
    suspend fun update(warehouse: Warehouse)

    @Delete
    suspend fun delete(warehouse: Warehouse)

    @Query("SELECT * from warehouse WHERE id = :id")
    fun getWarehouse(id: Int): Flow<Warehouse>

    @Query("SELECT * from warehouse ORDER BY name ASC")
    fun getAllWarehouses(): Flow<List<Warehouse>>
}