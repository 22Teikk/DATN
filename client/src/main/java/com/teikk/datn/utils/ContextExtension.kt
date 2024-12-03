package com.teikk.datn.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
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


fun Context.drawBitmapIntoVector(bitmap: Bitmap, vectorDrawable: Drawable): Bitmap {
    val resultBitmap = Bitmap.createBitmap(110, 125, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(resultBitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    val bitmapStartX = (canvas.width - bitmap.width) / 2f
    val bitmapStartY = ((canvas.height - bitmap.height) / 2f) - 12f
    canvas.drawBitmap(bitmap, bitmapStartX, bitmapStartY, null)
    return resultBitmap
}

fun Context.roundBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
    val startX = (bitmap.width - targetWidth) / 2
    val startY = (bitmap.height - targetHeight) / 2
    val targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(targetBitmap)
    canvas.drawBitmap(
        bitmap,
        Rect(startX, startY, startX + targetWidth, startY + targetHeight),
        Rect(0, 0, targetWidth, targetHeight),
        null
    )
    val roundedBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val radius = (targetWidth.coerceAtMost(targetHeight) / 2).toFloat()
    val rect = Rect(0, 0, targetWidth, targetHeight)
    val rectF = RectF(rect)
    val canvasRounded = Canvas(roundedBitmap)
    canvasRounded.drawRoundRect(rectF, radius, radius, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvasRounded.drawBitmap(targetBitmap, rect, rect, paint)
    return roundedBitmap
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

fun Context.getAddressByLocation(latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(this, Locale.getDefault())
    var result = ""
    try {
        val addresses: List<Address> =
            geocoder.getFromLocation(latitude, longitude, 1)!!.toMutableList()
        if (addresses.isNotEmpty()) {
            result =
                "${addresses[0].subAdminArea}, ${addresses[0].adminArea}, ${addresses[0].countryName}"
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}
