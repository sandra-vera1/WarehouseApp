package com.example.warehouseapp.goods

import com.example.warehouseapp.data.database.GoodsDao
import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.repositories.OfflineGoodsRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfflineGoodsRepositoryTest {

    private lateinit var goodsDao: GoodsDao
    private lateinit var repository: OfflineGoodsRepository

    @Before
    fun setUp() {
        goodsDao = mockk(relaxed = true)
        repository = OfflineGoodsRepository(goodsDao)
    }

    @Test
    fun getAllGoodsStream_returns_expected_flow() = runTest {
        val mockGoods = listOf(Goods(id = 1, name = "Item A", description = "Test", quantity = 10, image = "", warehouseId = null))
        every { goodsDao.getAllGoods() } returns flowOf(mockGoods)

        val result = repository.getAllGoodsStream()
        result.collect {
            assertEquals(mockGoods, it)
        }
    }

    @Test
    fun getGoodsStream_returns_single_goods() = runTest {
        val good = Goods(id = 2, name = "Item B", description = "Desc", quantity = 5, image = "", warehouseId = null)
        every { goodsDao.getGood(2) } returns flowOf(good)

        val result = repository.getGoodsStream(2)
        result.collect {
            assertEquals(good, it)
        }
    }

    @Test
    fun insertGoods_calls_insert_and_returns_id() = runTest {
        val good = Goods(name = "New", description = "Insert", quantity = 1, image = "", warehouseId = null)
        coEvery { goodsDao.insert(any()) } returns 5L

        val id = repository.insertGoods(good)
        assertEquals(5L, id)
        coVerify { goodsDao.insert(good) }
    }

    @Test
    fun updateGoods_calls_update() = runTest {
        val good = Goods(id = 3, name = "Update", description = "Update Desc", quantity = 3, image = "", warehouseId = null)
        coEvery { goodsDao.update(good) } just Runs

        repository.updateGoods(good)
        coVerify { goodsDao.update(good) }
    }

    @Test
    fun deleteGoods_calls_delete() = runTest {
        val good = Goods(id = 4, name = "Delete", description = "Desc", quantity = 2, image = "", warehouseId = null)
        coEvery { goodsDao.delete(good) } just Runs

        repository.deleteGoods(good)
        coVerify { goodsDao.delete(good) }
    }
}