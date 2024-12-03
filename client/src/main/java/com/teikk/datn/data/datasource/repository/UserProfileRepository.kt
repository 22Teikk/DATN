package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.datasource.local.UserProfileLocalRepository
import com.teikk.datn.data.datasource.remote.UserProfileRemoteRepository
import com.teikk.datn.data.model.UserProfile
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val userProfileRemoteRepository: UserProfileRemoteRepository,
    private val userProfileLocalRepository: UserProfileLocalRepository,
) {
    suspend fun insertUserProfile(userProfile: UserProfile) = userProfileLocalRepository.insertUserProfile(userProfile)
    suspend  fun saveUserToLocal(userProfile: UserProfile) = userProfileLocalRepository.updateUserProfile(userProfile)
    suspend fun saveUserToRemote(userProfile: UserProfile) = userProfileRemoteRepository.updateUserProfile(userProfile)
    suspend fun getUserProfileRemote(id : String) {
      val response = userProfileRemoteRepository.getUserProfile(id)
      if (response.isSuccessful) {
          val userProfile = response.body()!!
          userProfileLocalRepository.insertUserProfile(userProfile)
      }
    }
    suspend fun getUserProfileByID(id: String) = userProfileLocalRepository.getUserProfileByID(id)
    suspend fun deleteUserFromLocal(userProfile: UserProfile) = userProfileLocalRepository.deleteUserProfile(userProfile)
    suspend fun deleteAllUser() = userProfileLocalRepository.deleteAllUsers()
}