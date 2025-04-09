package com.example.warehouseapp.warehouse

import app.cash.turbine.test
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.data.repositories.WarehouseRepository
import com.example.warehouseapp.viewmodels.WarehouseViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WarehouseViewModelTest {

    private lateinit var warehouseRepository: WarehouseRepository
    private lateinit var warehouseViewModel: WarehouseViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        warehouseRepository = mockk()
        every { warehouseRepository.getAllWarehousesStream() } returns flowOf(emptyList())
        warehouseViewModel = WarehouseViewModel(warehouseRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun add_warehouse_should_call_insert_warehouse_on_repository() = runTest {
        val warehouse = Warehouse(id = 1, name = "Warehouse A", province = "ON", locationAddress = "123 St")
        every { warehouseRepository.getAllWarehousesStream() } returns flowOf(emptyList())
        coEvery { warehouseRepository.insertWarehouse(warehouse) } just Runs
        warehouseViewModel.addWarehouse(warehouse)
        advanceUntilIdle()
        coVerify { warehouseRepository.insertWarehouse(warehouse) }
    }

    @Test
    fun getWarehouse_should_return_warehouse_from_repository() = runTest {
        val warehouse = Warehouse(id = 5, name = "Warehouse A", province = "ON", locationAddress = "123 St")
        every { warehouseRepository.getWarehouseStream(5) } returns flowOf(warehouse)
        warehouseViewModel.getWarehouse(5).test {
            assertEquals(warehouse, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun update_warehouse_should_call_update_warehouse_on_repository() = runTest {
        val warehouse = Warehouse(id = 1, name = "Updated Warehouse", province = "ON", locationAddress = "456 St")
        every { warehouseRepository.getAllWarehousesStream() } returns flowOf(emptyList())
        coEvery { warehouseRepository.updateWarehouse(warehouse) } just Runs
        warehouseViewModel.updateWarehouse(warehouse)
        advanceUntilIdle()
        coVerify { warehouseRepository.updateWarehouse(warehouse) }
    }

    @Test
    fun delete_warehouse_should_call_delete_warehouse_on_repository() = runTest {
        val warehouse = Warehouse(id = 1, name = "Warehouse A", province = "ON", locationAddress = "123 St")
        every { warehouseRepository.getAllWarehousesStream() } returns flowOf(emptyList())
        coEvery { warehouseRepository.getWarehouseStream(1) } returns flowOf(warehouse)
        coEvery { warehouseRepository.deleteWarehouse(warehouse) } just Runs
        warehouseViewModel.deleteWarehouse(1)
        advanceUntilIdle()
        coVerify { warehouseRepository.deleteWarehouse(warehouse) }
    }
}
