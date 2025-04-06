package com.example.warehouseapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.warehouseapp.data.AppDataContainer
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
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = AppDataContainer(this)
        val warehouseViewModel = ViewModelProvider(
            this,
            WarehouseViewModel.Factory(appContainer.warehouseRepository)
        )[WarehouseViewModel::class.java]

        val goodsViewModel = ViewModelProvider(
            this,
            GoodsViewModel.Factory(appContainer.goodsRepository)
        )[GoodsViewModel::class.java]

        val userViewModel = ViewModelProvider(
            this,
            UserViewModel.Factory(appContainer.userRepository)
        )[UserViewModel::class.java]

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
    val context = LocalContext.current
    val navController = rememberNavController()
    WarehouseAppTheme {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "goods_list" else "login"
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = userViewModel,
                    onLoginSuccess = { user ->
                        SessionManager(context).saveUserSession(user)
                        navController.navigate("goods_list") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    showErrorMessage = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            composable("warehouse_list") {
                WarehousesListScreen(warehouseViewModel, goodsViewModel, navController)
            }
            composable("create_warehouse/{warehouseId}") { backStackEntry ->
                val warehouseId =
                    backStackEntry.arguments?.getString("warehouseId")?.toIntOrNull() ?: 0
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
                val goodsId = backStackEntry.arguments?.getString("goodsId")?.toIntOrNull() ?: 0
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