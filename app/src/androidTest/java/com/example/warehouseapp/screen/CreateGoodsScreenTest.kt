package com.example.warehouseapp.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.ui.screens.CreateGoodsScreen
import com.example.warehouseapp.util.viewmodel.FakeGoodsViewModel
import com.example.warehouseapp.util.viewmodel.FakeWarehouseViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateGoodsScreenTest {
    @get:Rule val composeTestRule = createComposeRule()

    private lateinit var fakeGoodsViewModel: FakeGoodsViewModel
    private lateinit var fakeWarehouseViewModel: FakeWarehouseViewModel
    private var wasNavigatedBack = false

    @Before
    fun setup() {
        fakeGoodsViewModel = FakeGoodsViewModel()
        fakeWarehouseViewModel = FakeWarehouseViewModel().apply {
            setWarehouses(listOf(Warehouse(1, "Main Warehouse", "ON", "Address", 0)))
        }
        wasNavigatedBack = false
    }

    @Test
    fun createGoods_withValidInput_callsAddGoods() {
        composeTestRule.setContent {
            CreateGoodsScreen(
                warehouseViewModel = fakeWarehouseViewModel,
                viewModel = fakeGoodsViewModel,
                navController = rememberNavController(),
                goodsId = 0,
                onNavigateBack = { wasNavigatedBack = true }
            )
        }
        composeTestRule.onNodeWithTag("Name").performTextInput("Laptop")
        composeTestRule.onNodeWithTag("Description").performTextInput("Gaming Laptop")
        composeTestRule.onNodeWithTag("Quantity").performTextInput("10")
        composeTestRule.onNodeWithText("Save").performClick()

        val saved = fakeGoodsViewModel.addedGoods
        assert(saved != null)
        assert(saved?.name == "Laptop")
        assert(saved?.description == "Gaming Laptop")
        assert(saved?.quantity == 10)
        assert(wasNavigatedBack)
    }


    @Test
    fun editWarehouse_displaysExistingData_andCallsUpdate() {
        val goods = Goods(1, "Sample", "Desc", 5, "1.jpg", 1)
        val warehouse1 = Warehouse(1, "Warehouse A", "ON", "123 Street", 10)

        fakeGoodsViewModel.setGoods(goods)

        val fakeWarehouseViewModel = FakeWarehouseViewModel().apply {
            setWarehouses(listOf(warehouse1))
        }

        composeTestRule.setContent {
            CreateGoodsScreen(
                warehouseViewModel = fakeWarehouseViewModel,
                viewModel = fakeGoodsViewModel,
                navController = rememberNavController(),
                goodsId = 1,
                onNavigateBack = { wasNavigatedBack = true }
            )
        }
        composeTestRule.onNodeWithText("Sample").assertIsDisplayed()
        composeTestRule.onNodeWithText("Desc").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Update").performClick()

        val updated = fakeGoodsViewModel.updatedGoods
        assert(updated != null)
        assert(updated?.id == 1)
        assert(wasNavigatedBack)
    }

    @Test
    fun allocateGoods_movesGoodsToNewWarehouse() {
        val goods = Goods(1, "Sample", "Desc", 5, "1.jpg", 1)
        val warehouse1 = Warehouse(1, "Warehouse A", "ON", "123 Street", 10)
        val warehouse2 = Warehouse(2, "Warehouse B", "ON", "456 Avenue", 20)

        fakeGoodsViewModel.setGoods(goods)

        val fakeWarehouseViewModel = FakeWarehouseViewModel().apply {
            setWarehouses(listOf(warehouse1, warehouse2))
        }

        var navigatedBack = false

        composeTestRule.setContent {
            CreateGoodsScreen(
                warehouseViewModel = fakeWarehouseViewModel,
                viewModel = fakeGoodsViewModel,
                navController = rememberNavController(),
                goodsId = goods.id,
                isAllocationPage = true,
                onNavigateBack = { navigatedBack = true }
            )
        }
        composeTestRule.onNodeWithTag("WarehouseDropdown").performClick()
        composeTestRule.onNodeWithText("Warehouse B").performClick()
        composeTestRule.onNodeWithTag("Allocate Goods").performClick()

        val updated = fakeGoodsViewModel.updatedGoods
        assert(updated != null)
        assert(updated?.warehouseId == 2)
        assert(navigatedBack)
    }
}