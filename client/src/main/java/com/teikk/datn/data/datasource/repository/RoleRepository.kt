package com.teikk.datn.data.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teikk.datn.data.datasource.local.RoleLocalRepository
import com.teikk.datn.data.datasource.remote.RoleRemoteRepository
import com.teikk.datn.data.model.Role
import javax.inject.Inject

class RoleRepository @Inject constructor(
    private val roleRemoteRepository: RoleRemoteRepository,
    private val roleLocalRepository: RoleLocalRepository,
){
    private val _rolesLiveData = MutableLiveData<List<Role>>()
    val rolesLiveData: LiveData<List<Role>> get() = _rolesLiveData
    suspend fun fetchRoleData()  {
        val response = roleRemoteRepository.getAllRoles()
        if (response.isSuccessful) {
            val roles = response.body()!!
            roleLocalRepository.insertRoles(roles)
            _rolesLiveData.postValue(roles)
        }
    }
}