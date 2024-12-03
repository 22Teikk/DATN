package com.teikk.datn.data.service

import com.google.gson.JsonObject
import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.model.Category
import com.teikk.datn.data.model.Feedback
import com.teikk.datn.data.model.Order
import com.teikk.datn.data.model.OrderItem
import com.teikk.datn.data.model.Payment
import com.teikk.datn.data.model.PaymentMethod
import com.teikk.datn.data.model.Product
import com.teikk.datn.data.model.Role
import com.teikk.datn.data.model.Store
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.data.model.Wishlist
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("api/v1/stores/77c611bd-9195-4d54-a48c-a526e16c31df")
    suspend fun getStoreByID() : Response<Store>
    // User Profile
    @PUT("api/v1/user_profiles/{id}")
    suspend fun updateUserProfile(@HeaderMap adminHeaders: Map<String, String> ,@Path("id") id: String,@Body userProfile: UserProfile) : Response<UserProfile>
    @GET("api/v1/user_profiles/{id}")
    suspend fun getAllUserProfileByID(
        @Path("id") id: String
    ): Response<UserProfile>
    // User Profile

    // Product API
    @GET("api/v1/products")
    suspend fun getAllProducts(@HeaderMap adminHeaders: Map<String, String>): Response<List<Product>>
    // Product API

    // Wishlist API
    @GET("api/v1/wishlists/user")
    suspend fun getWishlistForUser(
        @Query("uid") uid: String
    ): Response<List<Wishlist>>
    @POST("api/v1/wishlists")
    suspend fun addToWishlist(@Body wishlist: Wishlist): Response<Wishlist>
    @DELETE("api/v1/wishlists/{id}")
    suspend fun deleteFromWishlist(@Path("id") id: String): Response<JsonObject>
    // Wishlist API

    // Cart
    @GET("api/v1/carts/user")
    suspend fun getCartForUser(
        @Query("uid") uid: String
    ): Response<List<Cart>>
    @POST("api/v1/carts/user")
    suspend fun updateManyCarts(
        @Body carts: List<Cart>
    ): Response<JsonObject>
    @POST("api/v1/carts")
    suspend fun addToCart(@Body cart: Cart): Response<Cart>
    @DELETE("api/v1/carts/{id}")
    suspend fun deleteFromCart(@Path("id") id: String): Response<JsonObject>
    @PUT("api/v1/carts/{id}")
    suspend fun updateCart(@Path("id") id: String,@Body cart: Cart) : Response<Cart>
    // Cart

    // Order
    @GET("api/v1/orders/user")
    suspend fun getOrderForUser(
        @Query("uid") uid: String
    ): Response<List<Order>>
    @POST("api/v1/orders")
    suspend fun createOrder(@Body order: Order): Response<Order>
    @PUT("api/v1/orders/{id}")
    suspend fun updateOrder(@Path("id") id: String,@Body order: Order) : Response<Order>

    // Order Item
    @POST("api/v1/order_items")
    suspend fun createOrderItem(@Body order: OrderItem): Response<OrderItem>
    @GET("api/v1/order_items/user")
    suspend fun getOrderItemForOrder(
        @Query("order_id") orderId: String
    ): Response<List<OrderItem>>
    // Order Item

    // Payment
    @POST("api/v1/payments")
    suspend fun makePayment(@Body payment: Payment): Response<Payment>

    // Feedback
    @POST("api/v1/feedbacks")
    suspend fun makeFeedback(@Body feedback: Feedback): Response<Feedback>
    // Feedback


}