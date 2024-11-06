package com.teikk.datn.di

import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.data.datasource.remote.EntityRepository
import com.teikk.datn.data.datasource.service.ApiService
import com.teikk.datn.data.datasource.remote.AuthRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
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
object RetrofitModule {
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
    fun provideEntityRepository(apiService: ApiService): EntityRepository {
        return EntityRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthService(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils): AuthRepository {
        return AuthRepository(apiService, sharedPreferenceUtils)
    }

    @Provides
    @Singleton
    fun provideRoleService(apiService: ApiService, sharedPreferenceUtils: SharedPreferenceUtils): RoleRemoteRepository {
        return RoleRemoteRepository(apiService, sharedPreferenceUtils)
    }
}