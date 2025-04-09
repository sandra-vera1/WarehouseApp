package com.example.warehouseapp.goods

import app.cash.turbine.test
import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.repositories.GoodsRepository
import com.example.warehouseapp.viewmodels.GoodsViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GoodsViewModelTest {

    private lateinit var viewModel: GoodsViewModel
    private lateinit var repository: GoodsRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = GoodsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addGoods_should_call_insert_and_return_id() = runTest {
        val good = Goods(name = "Router", description = "WiFi", quantity = 100, image = "", warehouseId = null)
        coEvery { repository.insertGoods(any()) } returns 10L

        var returnedId = -1
        viewModel.addGoods(good) { returnedId = it }

        advanceUntilIdle()

        coVerify { repository.insertGoods(good) }
        assertEquals(10, returnedId)
    }

    @Test
    fun getGoods_should_return_good_from_repository() = runTest {
        val good = Goods(id = 5, name = "Monitor", description = "HD", quantity = 120, image = "", warehouseId = null)
        every { repository.getGoodsStream(5) } returns flowOf(good)

        viewModel.getGoods(5).test {
            assertEquals(good, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateGoods_should_call_repository() = runTest {
        val updatedGood = Goods(id = 2, name = "Keyboard", description = "Mechanical", quantity = 45, image = "", warehouseId = null)

        viewModel.updateGoods(updatedGood)
        advanceUntilIdle()

        coVerify { repository.updateGoods(updatedGood) }
    }

    @Test
    fun deleteGoods_should_call_deleteGoods_with_correct_item() = runTest {
        val good = Goods(id = 7, name = "Mouse", description = "Wireless", quantity = 50, image = "", warehouseId = null)
        every { repository.getGoodsStream(7) } returns flowOf(good)
        coEvery { repository.deleteGoods(good) } just Runs
    }
}