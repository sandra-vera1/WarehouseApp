package com.example.warehouseapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.ui.components.FooterBar
import com.example.warehouseapp.utils.SessionManager
import com.example.warehouseapp.utils.UserRoles.ROLE_ADMIN
import com.example.warehouseapp.viewmodels.GoodsViewModel
import com.example.warehouseapp.viewmodels.WarehouseViewModel
import kotlinx.coroutines.flow.filter


@Composable
fun WarehousesListScreen(
    viewModel: WarehouseViewModel,
    goodsViewModel: GoodsViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val role = sessionManager.getUserRole()

    val warehouses = viewModel.warehouses.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val goodsList = goodsViewModel.goodsL.collectAsStateWithLifecycle(initialValue = emptyList()).value

    val createNewWarehouse: () -> Unit = {
        navController.navigate("create_warehouse/0")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WoofTopAppBar("Warehouses", role)
        },
        bottomBar = {
            FooterBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    color = Color.White
                )
        ) {

            if (role == ROLE_ADMIN) {
                CreateNewButton(createNewWarehouse)
            }
            HorizontalDivider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(warehouses) { warehouse ->
                    val filteredGoods = goodsList.filter { it.warehouseId == warehouse.id }
                    val quantity = filteredGoods.sumOf { it.quantity }
                    val updatedWarehouse = warehouse.copy(totalGoods = quantity)

                    WarehouseItem(
                        viewModel,
                        warehouse = updatedWarehouse,
                        modifier = Modifier.padding(1.dp),
                        navController,
                        role
                    )
                }
            }
        }
    }
}

@Composable
private fun WarehouseItem(
    viewModel: WarehouseViewModel,
    warehouse: Warehouse,
    modifier: Modifier = Modifier,
    navController: NavController,
    role: Int
) {
    val context = LocalContext.current
    val editWarehouse: (Int) -> Unit = { id ->
        navController.navigate("create_warehouse/$id")
    }
    val deleteWarehouse: (Int) -> Unit = { id ->
        viewModel.deleteWarehouse(id)
        Toast.makeText(context, "Warehouse deleted successfully", Toast.LENGTH_SHORT).show()
    }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .background(
                    color = Color.White
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),

            )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                TopInformation(warehouse.id, "Total goods:", warehouse.totalGoods)
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(0.80f)) {
                    BottomInformation(
                        warehouse.name,
                        "${warehouse.province}, ${warehouse.locationAddress}"
                    )
                }
                if (role == ROLE_ADMIN) {
                    Column(modifier = Modifier.weight(0.20f)) {
                        ActionButtons(warehouse.id, editWarehouse, deleteWarehouse)
                    }
                }
            }
            HorizontalDivider(color = Color.Black, thickness = 1.dp)
        }
    }
}
