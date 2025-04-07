package com.example.warehouseapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.data.repositories.WarehouseRepository
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class WarehouseViewModel(private val warehouseRepository: WarehouseRepository) : ViewModel() {
    open val warehouses: Flow<List<Warehouse>> = warehouseRepository.getAllWarehousesStream()

    open fun getWarehouse(warehouseId: Int): Flow<Warehouse?> {
        return warehouseRepository.getWarehouseStream(warehouseId)
    }

    open fun addWarehouse(warehouse: Warehouse) {
        viewModelScope.launch {
            warehouseRepository.insertWarehouse(warehouse)
        }
    }

    open fun updateWarehouse(updatedWarehouse: Warehouse) {
        viewModelScope.launch {
            warehouseRepository.updateWarehouse(updatedWarehouse)
        }
    }

    fun deleteWarehouse(warehouseId: Int) {
        viewModelScope.launch {
            //TODO update warehouse id on goods
            val warehouse = getWarehouse(warehouseId)
            warehouse.collect { it?.let { warehouseRepository.deleteWarehouse(it) } }
        }
    }

    open fun getProvincesList(): List<String> {
        return listOf("AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QU", "SK")
    }

    companion object {
        fun Factory(repository: WarehouseRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WarehouseViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return WarehouseViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}