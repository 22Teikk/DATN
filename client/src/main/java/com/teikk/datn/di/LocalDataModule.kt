package com.teikk.datn.di

import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.service.dao.RoleDao
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
}