package com.example.warehouseapp.util.viewmodel

import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.util.repository.FakeGoodsRepository
import com.example.warehouseapp.viewmodels.GoodsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGoodsViewModel : GoodsViewModel(FakeGoodsRepository()) {
    var addedGoods: Goods? = null
    var updatedGoods: Goods? = null
    private val _goodsFlow = MutableStateFlow<Goods?>(null)

    fun setGoods(goods: Goods) {
        _goodsFlow.value = goods
    }

    override fun addGoods(goods: Goods, onResult: (Int) -> Unit) {
        addedGoods = goods.copy(id = 1)
        onResult(1)
    }

    override fun updateGoods(updatedGoods: Goods) {
        this.updatedGoods = updatedGoods
    }

    override fun getGoods(goodsId: Int): Flow<Goods?> = _goodsFlow
}