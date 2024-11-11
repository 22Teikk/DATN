package com.teikk.datn.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.uriToFile(uri: Uri): File? {
    val inputStream = contentResolver.openInputStream(uri)
    var tempFile: File? = null
    try {
        val photoFile: File = createImageFile()
        inputStream?.let { inputStream ->
            tempFile = photoFile
            inputStream.use { input ->
                photoFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }
    return tempFile
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp: String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}