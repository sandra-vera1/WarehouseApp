package com.example.warehouseapp.ui.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.deleteTempImage
import com.example.warehouseapp.moveImageToPermanentFolder
import com.example.warehouseapp.saveDefaultSvgAsPng
import com.example.warehouseapp.saveImageToTempFolder
import com.example.warehouseapp.ui.components.FooterBar
import com.example.warehouseapp.utils.SessionManager
import com.example.warehouseapp.viewmodels.GoodsViewModel
import com.example.warehouseapp.viewmodels.WarehouseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoodsScreen(
    warehouseViewModel: WarehouseViewModel,
    viewModel: GoodsViewModel,
    navController: NavController,
    goodsId: Int,
    isAllocationPage: Boolean = false,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val role = sessionManager.getUserRole()

    // Collect warehouses list
    val warehouses by warehouseViewModel.warehouses.collectAsStateWithLifecycle(initialValue = emptyList())

    // Collect goods data if editing
    val goods by viewModel.getGoods(goodsId).collectAsStateWithLifecycle(initialValue = null)

    // Default values for new goods
    var name by remember { mutableStateOf("") }
    var goodsDescription by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var selectedWarehouse by remember { mutableStateOf<Warehouse?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>("${context.filesDir}/default.png".toUri()) }

    LaunchedEffect(goods) {
        if (goods != null) {
            name = goods!!.name
            goodsDescription = goods!!.description
            quantity = goods!!.quantity
            selectedWarehouse =
                warehouses.find { it.id == goods!!.warehouseId } ?: warehouses.firstOrNull()
            selectedImageUri = "${context.filesDir}/${goods!!.image}".toUri()
        }
    }


    deleteTempImage(context)
    saveDefaultSvgAsPng(
        context,
        MaterialTheme.colorScheme.tertiaryContainer.toArgb(),
        MaterialTheme.colorScheme.tertiary.toArgb()
    )

    Scaffold(
        topBar = {
            WoofTopAppBar(
                if (isAllocationPage) "Allocation" else if (goodsId != 0) "Edit Goods" else "Create Goods",
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
            if (!isAllocationPage) {
                ImageInputWithPreview(selectedImageUri) { uri -> selectedImageUri = uri }
                Spacer(modifier = Modifier.height(16.dp))
            }


            TextFieldWithLabel("Name:", name, isAllocationPage, 1) { name = it }
            TextFieldWithLabel(
                "Description:",
                goodsDescription,
                isAllocationPage,
                3
            ) { goodsDescription = it }
            IntegerTextField(
                "Quantity",
                quantity,
                onValueChange = { quantity = it },
                isAllocationPage
            )


            if (isAllocationPage) {
                WarehouseDropdown(warehouses, selectedWarehouse) { selectedWarehouse = it }
            }

            val saveGoods: () -> Unit = {
                val goodsToSave =
                    Goods(goodsId, name, goodsDescription, quantity, "", selectedWarehouse?.id)
                if (goodsToSave.id == 0) {
                    viewModel.addGoods(goodsToSave) { newId ->
                        goodsToSave.id = newId
                        afterSave(goodsToSave, selectedImageUri, context, viewModel, navController)
                    }
                } else {
                    afterSave(goodsToSave, selectedImageUri, context, viewModel, navController)
                }
            }
            val buttonText =
                if (isAllocationPage) "Allocate Goods" else if (goodsId == 0) "Save" else "Update"

            if (isAllocationPage || goodsId == 0) {
                SaveButton(saveGoods, buttonText)
            } else {
                val allocateGoods: () -> Unit = {
                    navController.navigate("create_goods/$goodsId/true") {
                        popUpTo("create_goods") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                TwoButtons(saveGoods, allocateGoods, buttonText, "Allocate")
            }
        }
    }
}

fun afterSave(
    goods: Goods,
    imageUri: Uri?,
    context: Context,
    viewModel: GoodsViewModel,
    navController: NavController
) {
    goods.image = "${goods.id}.jpg"
    imageUri?.let { uri ->
        saveAndMoveImage(context, uri, goods.id)
    }
    viewModel.updateGoods(goods)
    navController.popBackStack()
}


@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    isReadOnly: Boolean,
    minLines: Int,
    onValueChange: (String) -> Unit
) {
    Text(label, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        readOnly = isReadOnly,
        minLines = minLines
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseDropdown(
    warehouses: List<Warehouse>,
    selectedWarehouse: Warehouse?,
    onSelected: (Warehouse) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Spacer(modifier = Modifier.height(24.dp))
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            readOnly = true,
            value = selectedWarehouse?.name ?: "Select Warehouse",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            warehouses.forEach { warehouse ->
                DropdownMenuItem(text = { Text(text = warehouse.name) }, onClick = {
                    onSelected(warehouse)
                    expanded = false
                })
            }
        }
    }
}

private fun saveAndMoveImage(context: Context, uri: Uri, goodsId: Int) {
    saveImageToTempFolder(context, uri)?.let {
        moveImageToPermanentFolder(context, goodsId.toString())
    }
}