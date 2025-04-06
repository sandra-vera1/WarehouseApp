package com.example.warehouseapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.warehouseapp.R
import com.example.warehouseapp.utils.SessionManager
import com.example.warehouseapp.viewmodels.FooterViewModel

@Composable
fun FooterBar(navController: NavController, footerViewModel: FooterViewModel = viewModel()) {
    val navigateToAnotherScreen = footerViewModel.navigateToAnotherScreen.value == true
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    LaunchedEffect(navigateToAnotherScreen) {
        if (navigateToAnotherScreen) {
            navController.navigate("destination_screen")
            footerViewModel.doneNavigating()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f),
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FooterButton(
                    text = "Warehouses",
                    iconRes = R.drawable.warehouse_24,
                    isSelected = currentRoute == "warehouse_list",
                    onClick = { navController.navigate("warehouse_list") }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FooterButton(
                    text = "Goods",
                    iconRes = R.drawable.package_2_24,
                    isSelected = currentRoute == "goods_list",
                    onClick = { navController.navigate("goods_list") }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FooterButton(
                    text = "Logout",
                    iconRes = R.drawable.logout_24,
                    isSelected = currentRoute == "logout",
                    onClick = { sessionManager.clearSession()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FooterButton(text: String, iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    val imageColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
        .padding(0.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(imageColor)
            )
            Text(text = text, color = contentColor)
        }

    }
}