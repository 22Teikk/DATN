package com.teikk.datn.di

import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.datasource.repository.RoleRepository
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
}