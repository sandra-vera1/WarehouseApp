package com.example.warehouseapp.data.repositories

import com.example.warehouseapp.data.models.Goods
import kotlinx.coroutines.flow.Flow

interface GoodsRepository {
    /**
     * Retrieve all the goods from the the given data source.
     */
    fun getAllGoodsStream(): Flow<List<Goods>>

    /**
     * Retrieve an goods from the given data source that matches with the [id].
     */
    fun getGoodsStream(id: Int): Flow<Goods?>

    /**
     * Insert goods in the data source
     */
    suspend fun insertGoods(goods: Goods)

    /**
     * Delete goods from the data source
     */
    suspend fun deleteGoods(goods: Goods)

    /**
     * Update goods in the data source
     */
    suspend fun updateGoods(goods: Goods)
}