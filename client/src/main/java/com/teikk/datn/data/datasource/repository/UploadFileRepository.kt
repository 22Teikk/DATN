package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.datasource.service.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadFileRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun uploadFile(file: File) : String? {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val response = apiService.uploadFile(body)
        if (response.isSuccessful) {
            return response.body()?.get("data")?.asString
            // Handle successful upload
            println("File uploaded successfully: ${response.body()}")
        } else {
            // Handle failure
            println("File upload failed: ${response.message()}")
            return null
        }
    }
}