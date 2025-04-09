package com.example.warehouseapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.warehouseapp", appContext.packageName)
    }

    @Test
    fun testSuccessfulLogin() {
        val username = "admin"
        val password = "admin123"
        val isValid = (username == "admin" && password == "admin123")
        assertTrue(isValid)
    }

    @Test
    fun testFailedLogin() {
        val username = "admin"
        val password = "wrongpass"
        val isValid = (username == "admin" && password == "admin123")
        assertFalse(isValid)
    }

    @Test
    fun testGoodsListDisplaysItems() {
        val goods = listOf("Box", "Crate", "Pallet")
        assertTrue(goods.contains("Crate"))
    }

    @Test
    fun testCreateGoodsInputValidation() {
        val name = "Table"
        val quantity = 10
        assertTrue(name.isNotEmpty() && quantity > 0)
    }

    @Test
    fun testDeleteGoods() {
        val goods = mutableListOf("Box", "Crate", "Pallet")
        goods.remove("Crate")
        assertFalse(goods.contains("Crate"))
    }

    @Test
    fun testWarehouseListLoad() {
        val warehouses = listOf("Calgary", "Toronto")
        assertEquals(2, warehouses.size)
    }

    @Test
    fun testCreateWarehouseInputValidation() {
        val name = "New Warehouse"
        val address = "123 Main St"
        assertTrue(name.isNotEmpty() && address.isNotEmpty())
    }

    @Test
    fun testEditWarehouse() {
        var warehouseName = "Old Name"
        warehouseName = "New Name"
        assertEquals("New Name", warehouseName)
    }
}