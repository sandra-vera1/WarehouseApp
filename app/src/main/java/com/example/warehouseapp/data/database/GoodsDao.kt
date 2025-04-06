package com.example.warehouseapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.warehouseapp.data.models.Goods
import kotlinx.coroutines.flow.Flow

@Dao
interface GoodsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goods: Goods) : Long

    @Update
    suspend fun update(goods: Goods)

    @Delete
    suspend fun delete(goods: Goods)

    @Query("SELECT * from goods WHERE id = :id")
    fun getGood(id: Int): Flow<Goods>

    @Query("SELECT * from goods ORDER BY name ASC")
    fun getAllGoods(): Flow<List<Goods>>
}