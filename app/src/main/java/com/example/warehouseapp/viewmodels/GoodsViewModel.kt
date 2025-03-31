package com.example.warehouseapp.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.warehouseapp.data.Goods
import com.example.warehouseapp.data.goodsList
import com.example.warehouseapp.deleteImage


class GoodsViewModel : ViewModel(){
    private val _goods = mutableStateListOf<Goods>()
    val goodsL: SnapshotStateList<Goods> get() = _goods

    init {
        _goods.addAll(goodsList)
    }

    fun getGoods(goodsId: Int) : Goods{
        val index = _goods.indexOfFirst { it.id == goodsId }
        return _goods[index]
    }

    fun getGoodsByWarehouseId(warehouseId: Int) : List<Goods> {
        val goodsList = _goods.filter { it.warehouseId == warehouseId }
        return goodsList
    }

    fun addGoods(goods: Goods) : Int {
        val maxIdGoods = _goods.maxByOrNull { it.id }
        val maxId = (maxIdGoods?.id ?: 0) + 1
        goods.id = maxId
        goods.warehouseId = null
        _goods.add(goods)
        return maxId
    }

    fun updateGoods(updatedGoods: Goods) {
        val index = _goods.indexOfFirst { it.id == updatedGoods.id }
        if (index != -1) {
            _goods[index] = updatedGoods
        }
    }

    fun deleteGoods(goods: Goods, context: Context) : Boolean {
        deleteImage(context, goods.id.toString())
        return _goods.removeAll { it.id == goods.id }
    }
}