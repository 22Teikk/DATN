package com.teikk.datn.di

import android.content.Context
import com.teikk.datn.data.datasource.service.DatabaseApp
import com.teikk.datn.data.datasource.service.dao.CategoryDao
import com.teikk.datn.data.datasource.service.dao.PaymentMethodDao
import com.teikk.datn.data.datasource.service.dao.RoleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) : DatabaseApp {
        return DatabaseApp.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideRoleDao(databaseApp: DatabaseApp) : RoleDao {
        return databaseApp.roleDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(databaseApp: DatabaseApp) : CategoryDao {
        return databaseApp.categoryDao()
    }

    @Singleton
    @Provides
    fun providePaymentMethodDao(databaseApp: DatabaseApp) : PaymentMethodDao {
        return databaseApp.paymentMethodDao()
    }
}