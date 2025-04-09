package com.example.warehouseapp.util

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import java.io.File

fun dummyContext(): Context {
    val context = mockk<Context>(relaxed = true)
    val filesDir = mockk<File>(relaxed = true)
    every { context.filesDir } returns filesDir
    every { filesDir.exists() } returns true
    return context
}