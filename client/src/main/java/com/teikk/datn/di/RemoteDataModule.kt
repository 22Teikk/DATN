package com.teikk.datn.di

import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.remote.AuthRepository
import com.teikk.datn.data.datasource.remote.CategoryRemoteRepository
import com.teikk.datn.data.datasource.remote.PaymentMethodRemoteRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.datasource.service.ApiService
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
    private const val BASE_URL = "http://192.168.1.253:5001/api/v1/"
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

}