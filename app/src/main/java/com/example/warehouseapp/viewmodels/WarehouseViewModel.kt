package com.example.warehouseapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.warehouseapp.data.Warehouse
import com.example.warehouseapp.data.warehousesList

class WarehouseViewModel : ViewModel(){
    private val _warehouses = mutableStateListOf<Warehouse>()
    val warehouses: SnapshotStateList<Warehouse> get() = _warehouses

    init {
        _warehouses.addAll(warehousesList)
    }

    fun getWarehouse(warehouseId: Int) : Warehouse{
        val index = _warehouses.indexOfFirst { it.id == warehouseId }
        return _warehouses[index];
    }

    fun addWarehouse(warehouse: Warehouse) {
        val maxIdWarehouse = _warehouses.maxByOrNull { it.id }
        val maxId = (maxIdWarehouse?.id ?: 0) + 1
        warehouse.id = maxId
        _warehouses.add(warehouse)
    }

    fun updateWarehouse(updatedWarehouse: Warehouse) {
        val index = _warehouses.indexOfFirst { it.id == updatedWarehouse.id }
        if (index != -1) {
            _warehouses[index] = updatedWarehouse
        }
    }

    fun deleteWarehouse(warehouseId: Int) : Boolean {
        return _warehouses.removeAll { it.id == warehouseId }
    }

    fun getProvincesList(): List<String> {
        return listOf("AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QU", "SK")
    }
}