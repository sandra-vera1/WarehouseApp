package com.example.warehouseapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warehouseapp.data.Goods
import com.example.warehouseapp.data.Warehouse
import com.example.warehouseapp.ui.components.FooterBar
import com.example.warehouseapp.viewmodels.GoodsViewModel
import com.example.warehouseapp.viewmodels.WarehouseViewModel

@Composable
fun GoodsListScreen(
    viewModel: GoodsViewModel,
    warehouseViewModel: WarehouseViewModel,
    navController: NavController
) {
    var selectedItems by remember { mutableStateOf(listOf<String>()) }
    var selectedItemIds by remember { mutableStateOf(listOf<Int>()) }
    val createNewGoods: () -> Unit = {
        navController.navigate("create_goods/-1/false")
    }
    val warehouses = warehouseViewModel.warehouses
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WoofTopAppBar("Goods")
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

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(0.37f)) {
                    CreateNewButton(createNewGoods)
                }
                Column(modifier = Modifier.weight(0.63f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        MultiSelectDropdown(
                            warehouses,
                            selectedItems,
                            onSelectionChanged = { newSelection, newIdSelection ->
                                selectedItems = newSelection
                                selectedItemIds = newIdSelection
                            }
                        )
                    }
                }
            }
            HorizontalDivider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val filteredGoods =
                    if (selectedItemIds.isEmpty()) {
                        viewModel.goodsL
                    } else {
                        viewModel.goodsL.filter { it.warehouseId in selectedItemIds }
                    }
                items(filteredGoods) { goods ->
                    val index = warehouses.indexOfFirst { it.id == goods.warehouseId }
                    if (index != -1) {
                        GoodsItem(
                            viewModel,
                            goods = goods,
                            modifier = Modifier.padding(1.dp),
                            navController,
                            warehouses[index]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoodsItem(
    viewModel: GoodsViewModel,
    goods: Goods,
    modifier: Modifier = Modifier,
    navController: NavController,
    warehouse: Warehouse
) {
    val context = LocalContext.current
    val editGoods: (Int) -> Unit = { id ->
        navController.navigate("create_goods/$id/false")
    }
    val deleteGoods: (Int) -> Unit = {
        if(viewModel.deleteGoods(goods, context)){
            val toast = Toast.makeText(context, "Goods deleted successfully", Toast.LENGTH_SHORT)
            toast.show()
        }
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
                Column(modifier = Modifier.weight(0.55f)) {
                    TopInformation(goods.id, "Quantity:", goods.quantity)
                }
                Column(modifier = Modifier.weight(0.45f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = warehouse.name,
                            fontSize = 18.sp,
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(0.15f)
                        .padding(end = 8.dp)
                ) {
                    ImageMiniature(context, goods.image)
                }
                Column(modifier = Modifier.weight(0.65f)) {
                    BottomInformation(goods.name, goods.description)
                }
                Column(modifier = Modifier.weight(0.20f)) {
                    ActionButtons(goods.id, editGoods, deleteGoods)
                }
            }

            HorizontalDivider(color = Color.Black, thickness = 1.dp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectDropdown(
    options: List<Warehouse>,
    selectedItems: List<String>,
    onSelectionChanged: (List<String>, List<Int>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedItemsSet = remember { mutableStateOf(selectedItems.toSet()) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredOptions = options.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            val filteredText = selectedItems.joinToString(", ")
            val textCropped =
                if (filteredText.length > 17) filteredText.take(14) + "... (${selectedItems.size})" else filteredText
            TextField(
                value = if (textCropped.isEmpty()) "Select warehouse" else textCropped,
                onValueChange = { searchQuery = it },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = !expanded }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            val updatedSelectedItems =
                                if (selectedItemsSet.value.contains(option.id.toString())) {
                                    selectedItemsSet.value - option.id.toString()
                                } else {
                                    selectedItemsSet.value + option.id.toString()
                                }
                            selectedItemsSet.value = updatedSelectedItems

                            val filteredList: List<Warehouse> =
                                options.filter { it.id.toString() in selectedItemsSet.value }

                            var filteredNames: List<String> = filteredList.map { it.name }
                            filteredNames =
                                if (options.size == filteredNames.size) listOf("All Warehouses") else filteredNames
                            onSelectionChanged(filteredNames, filteredList.map { it.id })
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = selectedItemsSet.value.contains(option.id.toString()),
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = option.name)
                            }
                        }
                    )
                }
            }
        }
    }
}