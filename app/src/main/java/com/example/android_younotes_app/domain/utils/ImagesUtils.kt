package com.example.android_younotes_app.domain.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

sealed class ImagesUtils {

    suspend fun ImagesUtils.saveImageToFile(image: Bitmap, fileName: String, context: Context): String {
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    }

    suspend fun ImagesUtils.loadImageFromFile(filePath: String): Bitmap {
        return BitmapFactory.decodeFile(filePath)
    }
}