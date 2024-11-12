package com.teikk.datn.di

import com.teikk.datn.data.datasource.local.CategoryLocalRepository
import com.teikk.datn.data.datasource.local.PaymentMethodLocalRepository
import com.teikk.datn.data.datasource.local.ProductLocalRepository
import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.local.UserProfileLocalRepository
import com.teikk.datn.data.datasource.remote.CategoryRemoteRepository
import com.teikk.datn.data.datasource.remote.PaymentMethodRemoteRepository
import com.teikk.datn.data.datasource.remote.ProductRemoteRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.datasource.remote.UserProfileRemoteRepository
import com.teikk.datn.data.datasource.repository.CategoryRepository
import com.teikk.datn.data.datasource.repository.PaymentMethodRepository
import com.teikk.datn.data.datasource.repository.ProductRepository
import com.teikk.datn.data.datasource.repository.RoleRepository
import com.teikk.datn.data.datasource.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRoleRepository(remoteRepository: RoleRemoteRepository, localRepository: RoleLocalRepository) = RoleRepository(remoteRepository, localRepository)

    @Provides
    @Singleton
    fun provideCategoryRepository(remoteRepository: CategoryRemoteRepository, localRepository: CategoryLocalRepository) = CategoryRepository(remoteRepository, localRepository)


    @Provides
    @Singleton
    fun providePaymentMethodRepository(remoteRepository: PaymentMethodRemoteRepository, localRepository: PaymentMethodLocalRepository) = PaymentMethodRepository(remoteRepository, localRepository)

    @Provides
    @Singleton
    fun provideUserProfileRepository(remoteRepository: UserProfileRemoteRepository, localRepository: UserProfileLocalRepository) = UserProfileRepository(remoteRepository, localRepository)

    @Provides
    @Singleton
    fun provideProductRepository(remoteRepository: ProductRemoteRepository, localRepository: ProductLocalRepository) = ProductRepository(remoteRepository, localRepository)
}