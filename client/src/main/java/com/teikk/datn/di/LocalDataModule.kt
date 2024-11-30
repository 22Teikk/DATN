package com.teikk.datn.di

import com.teikk.datn.data.datasource.local.CartLocalRepository
import com.teikk.datn.data.datasource.local.CategoryLocalRepository
import com.teikk.datn.data.datasource.local.PaymentMethodLocalRepository
import com.teikk.datn.data.datasource.local.ProductLocalRepository
import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.local.UserProfileLocalRepository
import com.teikk.datn.data.datasource.local.WishlistLocalRepository
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
    @Provides
    @Singleton
    fun provideRoleRepository(roleDao: RoleDao) = RoleLocalRepository(roleDao)

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao) = CategoryLocalRepository(categoryDao)

    @Provides
    @Singleton
    fun providePaymentMethodRepository(paymentMethodDao: PaymentMethodDao) = PaymentMethodLocalRepository(paymentMethodDao)

    @Provides
    @Singleton
    fun provideUserProfileRepository(userProfileDao: UserProfileDao) = UserProfileLocalRepository(userProfileDao)

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao) = ProductLocalRepository(productDao)

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao) = CartLocalRepository(cartDao)

    @Provides
    @Singleton
    fun provideWishlistRepository(wishlistDao: WishlistDao) = WishlistLocalRepository(wishlistDao)
}