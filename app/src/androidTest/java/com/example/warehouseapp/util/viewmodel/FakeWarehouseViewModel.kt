package com.example.warehouseapp.util.viewmodel

import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.util.repository.FakeWarehouseRepository
import com.example.warehouseapp.viewmodels.WarehouseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeWarehouseViewModel : WarehouseViewModel(FakeWarehouseRepository()) {

    private val _warehouseFlow = MutableStateFlow<Warehouse?>(null)
    val addedWarehouse: Warehouse? get() = _added
    val updatedWarehouse: Warehouse? get() = _updated
    private var _added: Warehouse? = null
    private var _updated: Warehouse? = null

    private var provinces = listOf<String>()

    private val _warehouses = MutableStateFlow<List<Warehouse>>(emptyList())
    override val warehouses: StateFlow<List<Warehouse>> get() = _warehouses

    fun setWarehouse(warehouse: Warehouse) {
        _warehouseFlow.value = warehouse
    }

    override fun getWarehouse(id: Int): StateFlow<Warehouse?> = _warehouseFlow

    override fun addWarehouse(warehouse: Warehouse) {
        _added = warehouse
    }

    override fun updateWarehouse(warehouse: Warehouse) {
        _updated = warehouse
    }

    fun setProvinces(provincesList: List<String>) {
        provinces = provincesList
    }

    override fun getProvincesList(): List<String> = provinces

    fun setWarehouses(list: List<Warehouse>) {
        _warehouses.value = list
    }
}