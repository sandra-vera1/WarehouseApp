package com.example.warehouseapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.ui.components.FooterBar
import com.example.warehouseapp.utils.SessionManager
import com.example.warehouseapp.viewmodels.WarehouseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWarehouseScreen(
    viewModel: WarehouseViewModel,
    navController: NavController,
    warehouseId: Int,
    onNavigateBack: () -> Unit
) {

    val warehouseFlow = remember(warehouseId) { viewModel.getWarehouse(warehouseId) }

    val provinces = viewModel.getProvincesList()
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val role = sessionManager.getUserRole()

    val warehouse by warehouseFlow.collectAsStateWithLifecycle(initialValue = null)

    var selectedProvince by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var locationAddress by remember { mutableStateOf("") }

    LaunchedEffect(warehouse) {
        if (warehouse != null) {
            name = warehouse!!.name
            selectedProvince = warehouse!!.province
            locationAddress = warehouse!!.locationAddress
        }
    }

    Scaffold(
        topBar = {
            WoofTopAppBar(
                if (warehouseId != 0) "Edit Warehouse" else "Create Warehouse",
                onNavigateBack,
                role
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
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            TextFieldWithLabel("Name:", name, 1) { name = it }
            TextFieldWithLabel("Location Address:", locationAddress, 3) { locationAddress = it }
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
                if (warehouseId == 0) {
                    viewModel.addWarehouse(warehouseToSave)
                } else {
                    viewModel.updateWarehouse(warehouseToSave)
                }
                navController.popBackStack()
            }
            val buttonText = if (warehouseId == 0) "Save" else "Update"
            SaveButton(saveWarehouse, buttonText)
        }
    }
}


@Composable
fun TextFieldWithLabel(label: String, value: String, minLines: Int, onValueChange: (String) -> Unit) {
    Text(label, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Enter $label") },
        modifier = Modifier.fillMaxWidth(),
        minLines= minLines
    )
    Spacer(modifier = Modifier.height(16.dp))
}