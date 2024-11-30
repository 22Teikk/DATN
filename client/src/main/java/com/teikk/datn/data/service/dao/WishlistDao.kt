package com.teikk.datn.data.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.teikk.datn.data.model.Wishlist

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWishlists(roles: List<Wishlist>)

    @Delete
    fun delete(paymentMethod: Wishlist)

    @Query("SELECT * FROM wishlist_tables")
    fun getAllWishlist(): Flow<List<Wishlist>>

    @Query("DELETE FROM wishlist_tables")
    fun deleteAllWishlists()

}