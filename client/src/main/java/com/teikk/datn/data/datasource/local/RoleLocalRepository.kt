package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.service.dao.RoleDao
import com.teikk.datn.data.model.Role
import javax.inject.Inject

class RoleLocalRepository @Inject constructor(
    private val roleDao: RoleDao
) {
    suspend fun getAllRoles() = roleDao.getAllRole()
    suspend fun insertRoles(roles: List<Role>) = roleDao.insertRoles(roles)
    suspend fun deleteRole(role: Role) = roleDao.delete(role)
}