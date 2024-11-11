package com.teikk.datn.data.datasource.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.teikk.datn.data.model.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserProfile)

    @Update
    suspend fun update(user: UserProfile)
    
    @Delete
    suspend fun delete(user: UserProfile)

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserByID(id: String): UserProfile

}