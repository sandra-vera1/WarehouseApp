package com.example.warehouseapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.warehouseapp.data.Warehouse
import com.example.warehouseapp.ui.components.FooterBar
import com.example.warehouseapp.viewmodels.WarehouseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWarehouseScreen(
    viewModel: WarehouseViewModel,
    navController: NavController,
    warehouseId: Int,
    onNavigateBack: () -> Unit
) {
    val warehouse = if (warehouseId != -1) {
        viewModel.getWarehouse(warehouseId)
    } else {
        Warehouse(-1, "", "", "", 0)
    }
    val provinces = viewModel.getProvincesList()
    var selectedProvince by remember { mutableStateOf(if (warehouse.province == "") provinces[0] else warehouse.province) }
    var name by remember { mutableStateOf(warehouse.name) }
    var locationAddress by remember { mutableStateOf(warehouse.locationAddress) }
    Scaffold(
        topBar = {
            WoofTopAppBar(
                if (warehouseId != -1) "Edit Warehouse" else "Create Warehouse",
                onNavigateBack
            )
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("Name:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter warehouse name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Location Address:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = locationAddress,
                onValueChange = { locationAddress = it },
                label = { Text("Enter location address") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Province:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedProvince,
                    onValueChange = { },
                    modifier = Modifier
                        .width(110.dp)
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    provinces.forEach { province ->
                        DropdownMenuItem(
                            text = { Text(text = province) },
                            onClick = {
                                selectedProvince = province
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(35.dp))

            val saveWarehouse: () -> Unit = {
                val warehouseToSave =
                    Warehouse(warehouseId, name, selectedProvince, locationAddress, 0)
                if (warehouseId == -1) {
                    viewModel.addWarehouse(warehouseToSave)
                } else {
                    viewModel.updateWarehouse(warehouseToSave)
                }
                navController.popBackStack()
            }
            val buttonText = if (warehouseId == -1) "Save" else "Update"
            SaveButton(saveWarehouse, buttonText)
        }
    }
}