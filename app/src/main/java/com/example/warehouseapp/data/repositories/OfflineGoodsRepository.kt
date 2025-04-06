package com.example.warehouseapp.data.repositories

import com.example.warehouseapp.data.database.GoodsDao
import com.example.warehouseapp.data.models.Goods
import kotlinx.coroutines.flow.Flow

class OfflineGoodsRepository(private val godsDao: GoodsDao) : GoodsRepository {
    override fun getAllGoodsStream(): Flow<List<Goods>> = godsDao.getAllGoods()

    override fun getGoodsStream(id: Int): Flow<Goods?> = godsDao.getGood(id)

    override suspend fun insertGoods(goods: Goods) : Long = godsDao.insert(goods)

    override suspend fun deleteGoods(goods: Goods) = godsDao.delete(goods)

    override suspend fun updateGoods(goods: Goods) = godsDao.update(goods)
}