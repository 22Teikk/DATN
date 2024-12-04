package com.teikk.datn.di

import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.remote.AuthRepository
import com.teikk.datn.data.datasource.remote.CartRemoteRepository
import com.teikk.datn.data.datasource.remote.CategoryRemoteRepository
import com.teikk.datn.data.datasource.remote.OrderItemRemoteRepository
import com.teikk.datn.data.datasource.remote.OrderRemoteRepository
import com.teikk.datn.data.datasource.remote.PaymentMethodRemoteRepository
import com.teikk.datn.data.datasource.remote.ProductRemoteRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.datasource.repository.UploadFileRepository
import com.teikk.datn.data.datasource.remote.UserProfileRemoteRepository
import com.teikk.datn.data.datasource.remote.WishlistRemoteRepository
import com.teikk.datn.data.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {
//    const val BASE_URL = "http://192.168.1.253:5001/"
    const val BASE_URL = "http://94.237.64.46:5001/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor =  HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideAPIService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils): AuthRepository {
        return AuthRepository(apiService, sharedPreferenceUtils)
    }
    @Provides
    @Singleton
    fun provideRoleRemote(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils) = RoleRemoteRepository(apiService, sharedPreferenceUtils)

    @Provides
    @Singleton
    fun provideCategoryRemote(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils) = CategoryRemoteRepository(apiService, sharedPreferenceUtils)

    @Provides
    @Singleton
    fun providePaymentMethodRemote(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils) = PaymentMethodRemoteRepository(apiService, sharedPreferenceUtils)

    @Provides
    @Singleton
    fun provideUserProfileRemote(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils) = UserProfileRemoteRepository(apiService, sharedPreferenceUtils)

    @Provides
    @Singleton
    fun provideUploadFile(apiService: ApiService) = UploadFileRepository(apiService)

    @Provides
    @Singleton
    fun provideProductRemote(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils) = ProductRemoteRepository(apiService, sharedPreferenceUtils)

    @Provides
    @Singleton
    fun provideWishlistRemote(apiService: ApiService) = WishlistRemoteRepository(apiService)

    @Provides
    @Singleton
    fun provideCartRemote(apiService: ApiService) = CartRemoteRepository(apiService)

    @Provides
    @Singleton
    fun provideOrderRemote(apiService: ApiService) = OrderRemoteRepository(apiService)

    @Provides
    @Singleton
    fun provideOrderItemRemote(apiService: ApiService) = OrderItemRemoteRepository(apiService)

}