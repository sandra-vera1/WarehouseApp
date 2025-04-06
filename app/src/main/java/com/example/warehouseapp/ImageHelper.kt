package com.example.warehouseapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun saveImageToTempFolder(context: Context, imageUri: Uri): File? {
    return try {
        val contentResolver = context.contentResolver
        val inputStream: InputStream = contentResolver.openInputStream(imageUri) ?: return null
        val tempFile = File(context.cacheDir, "temp_image.jpg")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun moveImageToPermanentFolder(context: Context, name: String): File? {
    try {
        val tempFile = File(context.cacheDir, "temp_image.jpg")

        if (!tempFile.exists()) {
            return null
        }
        val permanentFile = File(context.filesDir, "$name.jpg")

        tempFile.copyTo(permanentFile, overwrite = true)
        tempFile.delete()
        return permanentFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun deleteTempImage(context: Context) {
    val tempFile = File(context.cacheDir, "temp_image.jpg")
    if (tempFile.exists()) {
        tempFile.delete()
    }
}

fun deleteImage(context: Context, name: String?) {
    if (name.isNullOrEmpty()) {
        return
    }

    val tempFile = File(context.filesDir, "$name.jpg")
    if (tempFile.exists()) {
        tempFile.delete()
    }
}

fun saveDefaultSvgAsPng(context: Context, @ColorInt tintColor: Int, @ColorInt backgroundColor: Int) {
    val file = File(context.filesDir, "default.png")
    if (!file.exists()) {
        val drawable = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.image_24,
            null
        ) as? VectorDrawable ?: return

        drawable.setTint(tintColor)

        val canvasWidth = 2000
        val canvasHeight = 1200
        val drawableSize = 600

        val bitmap = createBitmap(canvasWidth, canvasHeight)
        val canvas = Canvas(bitmap)

        canvas.drawColor(backgroundColor)

        val left = (canvasWidth - drawableSize) / 2
        val top = (canvasHeight - drawableSize) / 2
        val right = left + drawableSize
        val bottom = top + drawableSize

        drawable.setBounds(left, top, right, bottom)
        drawable.draw(canvas)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
    }
}