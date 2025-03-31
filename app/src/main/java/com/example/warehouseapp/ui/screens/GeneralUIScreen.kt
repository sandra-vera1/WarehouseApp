package com.example.warehouseapp.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.warehouseapp.R
import com.example.warehouseapp.data.ROLE_ADMIN
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(pageName: String, role: Int) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                TitleTopBar(pageName, role)
            }
        )
        HorizontalDivider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(pageName: String, onNavigateBack: () -> Unit, role: Int) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                TitleTopBar(pageName, role)
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        HorizontalDivider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TitleTopBar(pageName: String, role: Int) {
    val brownColor = colorResource(id = R.color.brown)
    val roseLight = colorResource(id = R.color.rose_light)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.80f)) {
                Text(
                    text = pageName,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 30.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.20f)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(roseLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if(role == ROLE_ADMIN) ImageVector.vectorResource(R.drawable.font_a_24dp) else ImageVector.vectorResource(R.drawable.font_e_24dp),
                        contentDescription = "User",
                        tint = brownColor,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun TopInformation(
    id: Int,
    additionalText: String,
    quantity: Int
) {
    Column {
        Text(
            text = "Id: $id $additionalText $quantity",// TODO: SVG
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomInformation(
    topText: String,
    bottomText: String,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = topText,
                fontSize = 18.sp
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = bottomText,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ActionButtons(
    id: Int,
    editAction: (Int) -> Unit,
    deleteAction: (Int) -> Unit
) {
    val buttonColor = colorResource(id = R.color.brown)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            onClick = { editAction(id) },
            modifier = Modifier
                .weight(0.5f)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.edit_square_24),
                contentDescription = "Edit",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Button(
            onClick = { deleteAction(id) },
            modifier = Modifier
                .weight(0.5f)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.delete_24),
                contentDescription = "Delete",
                colorFilter = ColorFilter.tint(Color.White)
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
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .wrapContentWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(text = "Create New", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun FnButton(onClick: () -> Unit, buttonText: String) {
    val buttonColor = colorResource(id = R.color.brown)
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(text = buttonText, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SaveButton(onClick: () -> Unit, buttonText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        FnButton(onClick, buttonText)
    }
}


@Composable
fun TwoButtons(
    firstOnClick: () -> Unit,
    secondOnClick: () -> Unit,
    firstButtonText: String,
    secondButtonText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
    ) {
        FnButton(firstOnClick, firstButtonText)
        FnButton(secondOnClick, secondButtonText)
    }
}

@Composable
fun IntegerTextField(
    quantity: Int,
    onValueChange: (Int) -> Unit,
    readOnly: Boolean
) {
    TextField(
        value = quantity.toString(),
        onValueChange = { newText ->
            val newValue = newText.toIntOrNull()
            if (newValue != null) {
                onValueChange(newValue)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        readOnly = readOnly
    )
}

@Composable
fun ImageInputWithPreview(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onImageSelected(uri)
        }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = "Selected Image or Default",
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(2.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.brown),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Add an image")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { launcher.launch("image/*") }
                .align(Alignment.Start)
        )
    }
}

@Composable
fun ImageMiniature(
    context: Context,
    goodsImage: String
) {
    val file = File(context.filesDir, goodsImage)
    val imageFileName = if (file.exists()) goodsImage else "default.png"
    var selectedImageUri by remember { mutableStateOf("${context.filesDir}/$imageFileName".toUri()) }

    LaunchedEffect(goodsImage) {
        selectedImageUri = "${context.filesDir}/$imageFileName".toUri()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = "Miniature image",
            modifier = Modifier
                .width(60.dp)
                .height(50.dp)
                .padding(3.dp),
            contentScale = ContentScale.Crop
        )
    }
}
