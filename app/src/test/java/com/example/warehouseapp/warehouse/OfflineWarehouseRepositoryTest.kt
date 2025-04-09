package com.example.warehouseapp.warehouse

import com.example.warehouseapp.data.database.WarehouseDao
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.data.repositories.OfflineWarehouseRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfflineWarehouseRepositoryTest {

    private lateinit var warehouseDao: WarehouseDao
    private lateinit var warehouseRepository: OfflineWarehouseRepository

    @Before
    fun setUp() {
        warehouseDao = mockk()
        warehouseRepository = OfflineWarehouseRepository(warehouseDao)
    }

    @Test
    fun getAllWarehouseStream_returns_expected_flow() = runTest {
        val mockWarehouse = listOf(Warehouse(1, "Warehouse A", "ON", "123 St"))
        every { warehouseDao.getAllWarehouses() } returns flowOf(mockWarehouse)

        val result = warehouseRepository.getAllWarehousesStream()
        result.collect {
            assertEquals(mockWarehouse, it)
        }
    }

    @Test
    fun getWarehouseStream_returns_single_warehouse() = runTest {
        val warehouse = Warehouse(2, "Warehouse A", "ON", "123 St")
        every { warehouseDao.getWarehouse(2) } returns flowOf(warehouse)

        val result = warehouseRepository.getWarehouseStream(2)
        result.collect {
            assertEquals(warehouse, it)
        }
    }

    @Test
    fun insertWarehouse_calls_insert() = runTest {
        val warehouse = Warehouse(3, "Warehouse A", "ON", "123 St")
        coEvery { warehouseDao.insert(warehouse) } just Runs
        warehouseRepository.insertWarehouse(warehouse)
        coVerify { warehouseDao.insert(warehouse) }
    }

    @Test
    fun updateWarehouse_calls_update() = runTest {
        val warehouse = Warehouse(4, "Warehouse A", "ON", "123 St")
        coEvery { warehouseDao.update(warehouse) } just Runs
        warehouseRepository.updateWarehouse(warehouse)
        coVerify { warehouseDao.update(warehouse) }
    }

    @Test
    fun deleteWarehouse_calls_delete() = runTest {
        val warehouse = Warehouse(5, "Warehouse A", "ON", "123 St")
        coEvery { warehouseDao.delete(warehouse) } just Runs
        warehouseRepository.deleteWarehouse(warehouse)
        coVerify { warehouseDao.delete(warehouse) }
    }
}