package com.example.warehouseapp.util.repository

import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.repositories.GoodsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGoodsRepository : GoodsRepository {
    override fun getAllGoodsStream(): Flow<List<Goods>> {
        return flowOf()
    }

    override fun getGoodsStream(id: Int): Flow<Goods?> {
        return flowOf(null)
    }

    override suspend fun insertGoods(goods: Goods): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGoods(goods: Goods) {
        // no-op
    }

    override suspend fun updateGoods(goods: Goods) {
        // no-op
    }

}