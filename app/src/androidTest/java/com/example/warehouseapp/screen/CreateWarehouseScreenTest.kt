package com.example.warehouseapp.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.ui.screens.CreateWarehouseScreen
import com.example.warehouseapp.util.viewmodel.FakeWarehouseViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateWarehouseScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeViewModel: FakeWarehouseViewModel
    private var wasNavigatedBack = false

    @Before
    fun setup() {
        fakeViewModel = FakeWarehouseViewModel().apply {
            setProvinces(listOf("AB", "ON"))
        }

        wasNavigatedBack = false
    }

    @Test
    fun createWarehouse_withValidInput_callsAddWarehouse() {
        composeTestRule.setContent {
            CreateWarehouseScreen(
                viewModel = fakeViewModel,
                navController = rememberNavController(),
                warehouseId = 0,
                onNavigateBack = { wasNavigatedBack = true }
            )
        }
        composeTestRule.onNodeWithTag("Name").performTextInput("New Warehouse")
        composeTestRule.onNodeWithTag("Location Address").performTextInput("123 New Street")
        composeTestRule.onNodeWithTag("ProvinceDropdown").performClick()
        composeTestRule.onNodeWithText("ON").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        val saved = fakeViewModel.addedWarehouse
        assert(saved != null)
        assert(saved?.name == "New Warehouse")
        assert(saved?.province == "ON")
        assert(saved?.locationAddress == "123 New Street")
        assert(wasNavigatedBack)
    }

    @Test
    fun editWarehouse_displaysExistingData_andCallsUpdate() {
        val existingWarehouse = Warehouse(1, "Old Warehouse", "AB", "456 Old Street", 10)
        fakeViewModel.setWarehouse(existingWarehouse)

        composeTestRule.setContent {
            CreateWarehouseScreen(
                viewModel = fakeViewModel,
                navController = rememberNavController(),
                warehouseId = 1,
                onNavigateBack = { wasNavigatedBack = true }
            )
        }
        composeTestRule.onNodeWithText("Old Warehouse").assertIsDisplayed()
        composeTestRule.onNodeWithText("456 Old Street").assertIsDisplayed()
        composeTestRule.onNodeWithText("Update").performClick()

        val updated = fakeViewModel.updatedWarehouse
        assert(updated != null)
        assert(updated?.id == 1)
        assert(wasNavigatedBack)
    }
}