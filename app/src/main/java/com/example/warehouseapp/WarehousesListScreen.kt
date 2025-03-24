package com.example.warehouseapp

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.warehouseapp.data.Warehouse
import com.example.warehouseapp.data.warehouses

enum class WarehousesListScreen(@StringRes val title: Int){
    Start(title = R.string.app_name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(){
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.image_size))
                        .padding(dimensionResource(R.dimen.padding_small))
                        .clip(MaterialTheme.shapes.small),

                    )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 30.sp
                )
            }
        }
    )



}

@Composable
fun WarehousesListScreen() {
    val createNewWarehouse: () -> Unit = {
        println("Create new warehouse") // TODO: Implement warehouse creation logic
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WoofTopAppBar()
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
            CreateNewButton(createNewWarehouse)
            HorizontalDivider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(warehouses) { warehouse ->
                    DogItem(
                        warehouse = warehouse,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DogItem(
    warehouse : Warehouse,
    modifier: Modifier=Modifier
){
    val editWarehouse: (Int) -> Unit = { id ->
        println("Edit clicked for warehouse ID: $id")//TODO Edit warehouse
    }
    val deleteWarehouse: (Int) -> Unit = { id ->
        println("Delete clicked for warehouse ID: $id")//TODO Delete warehouse
    }

    Card(
        modifier=modifier
    ){
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
                TopInformation(1, 500)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(0.80f)) {
                    BottomInformation("Royal Warehouse", "AB, Spring Woods 250 SW")
                }
                Column(modifier = Modifier.weight(0.20f)) {
                    ActionButtons(1, editWarehouse, deleteWarehouse)
                }
            }

            HorizontalDivider(color = Color.Black, thickness = 1.dp)
        }
    }
}

@Composable
fun TopInformation(
    id: Int,
    totalGoods: Int
) {
    Column{
        Text(text = "Id: $id Total goods:$totalGoods",//stringResource(dogAge), TODO: SVG
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomInformation(
    fullName: String,
    fullAddress: String,
) {
    Column{
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = fullName,
                fontSize = 18.sp
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = fullAddress,
                fontSize = 14.sp
            )
        }
    }
}


@Composable
fun ActionButtons(
    id: Int,
    editWarehouse: (Int) -> Unit,
    deleteWarehouse: (Int) -> Unit
) {
    val buttonColor = colorResource(id = R.color.brown)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            onClick = { editWarehouse(id) },
            modifier = Modifier
                .weight(0.50f)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RectangleShape,
            contentPadding = PaddingValues(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.White
            )
        }

        Button(
            onClick = { deleteWarehouse(id) },
            modifier = Modifier
                .weight(0.50f)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RectangleShape,
            contentPadding = PaddingValues(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CreateNewButton(onClick: () -> Unit) {
    val buttonColor = colorResource(id = R.color.brown)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Button(
            onClick = onClick,
            modifier = Modifier
                .wrapContentWidth()
                .height(48.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(text = "Create New", color = Color.White)
        }
    }
}