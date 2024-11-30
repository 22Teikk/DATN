package com.teikk.datn.di

import android.content.Context
import com.teikk.datn.data.service.DatabaseApp
import com.teikk.datn.data.service.dao.CartDao
import com.teikk.datn.data.service.dao.CategoryDao
import com.teikk.datn.data.service.dao.PaymentMethodDao
import com.teikk.datn.data.service.dao.ProductDao
import com.teikk.datn.data.service.dao.RoleDao
import com.teikk.datn.data.service.dao.UserProfileDao
import com.teikk.datn.data.service.dao.WishlistDao
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

    @Singleton
    @Provides
    fun provideUserProfileDao(databaseApp: DatabaseApp) : UserProfileDao {
        return databaseApp.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(databaseApp: DatabaseApp) : ProductDao {
        return databaseApp.productDao()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(databaseApp: DatabaseApp) : WishlistDao {
        return databaseApp.wishlistDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(databaseApp: DatabaseApp) : CartDao {
        return databaseApp.cartDao()
    }
}