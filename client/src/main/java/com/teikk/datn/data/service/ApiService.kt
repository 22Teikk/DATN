package com.teikk.datn.data.service

import com.google.gson.JsonObject
import com.teikk.datn.data.model.Category
import com.teikk.datn.data.model.PaymentMethod
import com.teikk.datn.data.model.Role
import com.teikk.datn.data.model.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("api/v1/user_profiles/login")
    suspend fun login(@Body userProfile: UserProfile) : Response<JsonObject>

    @POST("api/v1/user_profiles/register")
    suspend fun register(@Body userProfile: UserProfile) : Response<JsonObject>

    @Multipart
    @POST("/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
    ): Response<JsonObject>

    @GET("api/v1/roles")
    suspend fun getAllRoles(@HeaderMap header: Map<String, String>) : Response<List<Role>>

    @GET("api/v1/payment_methods")
    suspend fun getAllPaymentMethods(@HeaderMap adminHeaders: Map<String, String>): Response<List<PaymentMethod>>

    @GET("api/v1/categories")
    suspend fun getAllCategories(@HeaderMap adminHeaders: Map<String, String>): Response<List<Category>>

    // User Profile
    @PUT("api/v1/user_profiles/{id}")
    suspend fun updateUserProfile(@HeaderMap adminHeaders: Map<String, String> ,@Path("id") id: String,@Body userProfile: UserProfile) : Response<UserProfile>
    // User Profile
}