package com.teikk.datn.data.datasource.service

import com.google.gson.JsonObject
import com.teikk.datn.data.model.Role
import com.teikk.datn.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiService {
    @GET("entities")
    suspend fun getAllEntities(@HeaderMap header: Map<String, String>) : Response<String>

    @POST("user_profiles/login")
    suspend fun login(@Body userProfile: UserProfile) : Response<JsonObject>

    @POST("user_profiles/register")
    suspend fun register(@Body userProfile: UserProfile) : Response<JsonObject>

    @GET("roles")
    suspend fun getAllRoles(@HeaderMap header: Map<String, String>) : Response<List<Role>>
}