package com.teikk.datn.data.service.dao

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
    fun insert(user: UserProfile)

    @Update
    fun update(user: UserProfile)
    
    @Delete
    fun delete(user: UserProfile)

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserByID(id: String): UserProfile

    @Query("DELETE FROM user_table")
    fun deleteAllUsers()
}