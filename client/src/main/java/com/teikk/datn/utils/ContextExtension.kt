package com.teikk.datn.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
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

fun Context.getAddressShareLocation(latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(this, Locale.getDefault())
    var result = ""
    try {
        val addresses: List<Address> =
            geocoder.getFromLocation(latitude, longitude, 1)!!.toMutableList()
        if (addresses.isNotEmpty()) {
            if (!addresses[0].subThoroughfare.isNullOrEmpty() && !addresses[0].thoroughfare.isNullOrEmpty())
                result =
                    "${addresses[0].subThoroughfare}, ${addresses[0].thoroughfare}, ${addresses[0].subAdminArea},${addresses[0].adminArea}"
            else if (addresses[0].subThoroughfare.isNullOrEmpty() && !addresses[0].thoroughfare.isNullOrEmpty())
                result =
                    "${addresses[0].thoroughfare}, ${addresses[0].subAdminArea},${addresses[0].adminArea}"
            else if (!addresses[0].subThoroughfare.isNullOrEmpty() && addresses[0].thoroughfare.isNullOrEmpty())
                result =
                    "${addresses[0].subThoroughfare}, ${addresses[0].subAdminArea},${addresses[0].adminArea}"
            else result =
                "${addresses[0].subAdminArea},${addresses[0].adminArea}"
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}
