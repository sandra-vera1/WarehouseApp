package com.example.warehouseapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.repositories.GoodsRepository
import com.example.warehouseapp.deleteImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.java


class GoodsViewModel(private val goodsRepository: GoodsRepository) : ViewModel(){
    val goodsL: Flow<List<Goods>> = goodsRepository.getAllGoodsStream()

    fun getGoods(goodsId: Int): Flow<Goods?> {
        return goodsRepository.getGoodsStream(goodsId)
    }

    fun addGoods(goods: Goods) : Int {
        viewModelScope.launch {
            goodsRepository.insertGoods(goods)
        }
        //TODO Get the inserted id
        return goods.id
    }

    fun updateGoods(updatedGoods: Goods) {
        viewModelScope.launch {
            goodsRepository.updateGoods(updatedGoods)
        }
    }

    fun deleteGoods(goodsId: Int, context: Context) {
        deleteImage(context, goodsId.toString())
        viewModelScope.launch {
            val goods = getGoods(goodsId)
            goods.collect { it?.let { goodsRepository.deleteGoods(it) } }
        }
    }

    companion object {
        fun Factory(repository: GoodsRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(GoodsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return GoodsViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}