package com.teikk.datn.data.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.model.Wishlist

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCarts(roles: List<Cart>)

    @Delete
    fun delete(paymentMethod: Cart)

    @Query("SELECT * FROM cart_tables")
    fun getAllCart(): Flow<List<Cart>>

    @Query("DELETE FROM cart_tables")
    fun deleteAllCarts()

}