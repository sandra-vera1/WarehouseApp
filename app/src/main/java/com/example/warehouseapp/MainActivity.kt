package com.example.warehouseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.warehouseapp.data.AppDataContainer
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