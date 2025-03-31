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
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.warehouseapp.data.Goods
import com.example.warehouseapp.deleteTempImage
import com.example.warehouseapp.moveImageToPermanentFolder
import com.example.warehouseapp.saveDefaultSvgAsPng
import com.example.warehouseapp.saveImageToTempFolder
import com.example.warehouseapp.ui.components.FooterBar
import com.example.warehouseapp.utils.SessionManager
import com.example.warehouseapp.viewmodels.GoodsViewModel
import com.example.warehouseapp.viewmodels.WarehouseViewModel
import java.io.File

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
    val goods = if (goodsId != -1) {
        viewModel.getGoods(goodsId)
    } else {
        Goods(-1, "", "", 0, "default.png", -1)
    }
    val warehouses = warehouseViewModel.warehouses
    var selectedWarehouse by remember {
        mutableStateOf(if (goods.warehouseId == -1) warehouses[0] else goods.warehouseId?.let {
            warehouseViewModel.getWarehouse(
                it
            )
        })
    }
    val context = LocalContext.current
    var name by remember { mutableStateOf(goods.name) }
    var goodsDescription by remember { mutableStateOf(goods.description) }
    var quantity by remember { mutableIntStateOf(goods.quantity) }

    val file = File(context.filesDir, goods.image)
    val imageFileName = if (file.exists()) goods.image else "default.png"

    val sessionManager = SessionManager(context)
    val role = sessionManager.getUserRole()

    var selectedImageUri by remember { mutableStateOf<Uri?>("${context.filesDir}/$imageFileName".toUri()) }
    deleteTempImage(context)
    saveDefaultSvgAsPng(context)
    Scaffold(
        topBar = {
            WoofTopAppBar(
                if (isAllocationPage) "Allocation" else if (goodsId != -1) "Edit Goods" else "Create Goods",
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
                    color = Color.White
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            if (!isAllocationPage) {
                ImageInputWithPreview(selectedImageUri) { uri ->
                    selectedImageUri = uri
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            Text("Name:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter goods name") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = isAllocationPage
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Description:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = goodsDescription,
                onValueChange = { goodsDescription = it },
                label = { Text("Enter goods description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                readOnly = isAllocationPage
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quantity:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            IntegerTextField(
                quantity,
                onValueChange = { quantity = it },
                isAllocationPage
            )


            if (isAllocationPage) {
                Text("Warehouse:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    selectedWarehouse?.let {
                        TextField(
                            readOnly = true,
                            value = it.name,
                            onValueChange = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        warehouses.forEach { warehouse ->
                            DropdownMenuItem(
                                text = { Text(text = warehouse.name) },
                                onClick = {
                                    selectedWarehouse = warehouse
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(35.dp))
            }

            val saveGoods: () -> Unit = {
                val goodsToSave =
                    Goods(goodsId, name, goodsDescription, quantity, "", selectedWarehouse?.id)
                if (goodsId == -1) {
                    goodsToSave.id = viewModel.addGoods(goodsToSave)
                }
                goodsToSave.image = "${goodsToSave.id}.jpg"

                selectedImageUri?.let { uri ->
                    saveAndMoveImage(context, uri, goodsToSave.id)
                }
                viewModel.updateGoods(goodsToSave)
                navController.popBackStack()
            }
            val buttonText =
                if (isAllocationPage) "Allocate Goods" else if (goodsId == -1) "Save" else "Update"

            if (isAllocationPage || goodsId == -1) {
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

private fun saveAndMoveImage(context: Context, uri: Uri, goodsId: Int) {
    saveImageToTempFolder(context, uri)?.let {
        moveImageToPermanentFolder(context, goodsId.toString())
    }
}