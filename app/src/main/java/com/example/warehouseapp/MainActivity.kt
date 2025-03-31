package com.example.warehouseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.warehouseapp.ui.screens.CreateGoodsScreen
import com.example.warehouseapp.ui.screens.CreateWarehouseScreen
import com.example.warehouseapp.ui.screens.GoodsListScreen
import com.example.warehouseapp.ui.screens.LoginScreen
import com.example.warehouseapp.ui.screens.WarehousesListScreen
import com.example.warehouseapp.ui.theme.WarehouseAppTheme
import com.example.warehouseapp.utils.SessionManager
import com.example.warehouseapp.viewmodels.GoodsViewModel
import com.example.warehouseapp.viewmodels.UserViewModel
import com.example.warehouseapp.viewmodels.WarehouseViewModel

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val warehouseViewModel: WarehouseViewModel by viewModels()
    private val goodsViewModel: GoodsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val sessionManager = SessionManager(this)
        val isLoggedIn = sessionManager.isLoggedIn()


        setContent {
            WarehouseApp(userViewModel, warehouseViewModel, goodsViewModel, isLoggedIn)
        }
    }
}

@Composable
fun WarehouseApp(
    userViewModel: UserViewModel,
    warehouseViewModel: WarehouseViewModel,
    goodsViewModel: GoodsViewModel,
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()
    WarehouseAppTheme {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "goods_list" else "login"
        ) {
            composable("login") {
                LoginScreen(userViewModel, navController)
            }
            composable("warehouse_list") {
                WarehousesListScreen(warehouseViewModel, goodsViewModel, navController)
            }
            composable("create_warehouse/{warehouseId}") { backStackEntry ->
                val warehouseId =
                    backStackEntry.arguments?.getString("warehouseId")?.toIntOrNull() ?: -1
                CreateWarehouseScreen(
                    warehouseViewModel,
                    navController,
                    warehouseId,
                    onNavigateBack = { navController.popBackStack() })
            }
            composable("goods_list") {
                GoodsListScreen(goodsViewModel, warehouseViewModel, navController)
            }
            composable("create_goods/{goodsId}/{isAllocationPage}") { backStackEntry ->
                val goodsId = backStackEntry.arguments?.getString("goodsId")?.toIntOrNull() ?: -1
                val isAllocationPage =
                    backStackEntry.arguments?.getString("isAllocationPage")?.toBoolean() == true

                CreateGoodsScreen(
                    warehouseViewModel,
                    goodsViewModel,
                    navController,
                    goodsId,
                    isAllocationPage,
                    onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}