package com.teikk.datn.data.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.teikk.datn.data.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(order: List<Order>)

    @Delete
    fun delete(order: Order)

    @Query("SELECT * FROM order_tables")
    fun getAllOrder(): Flow<List<Order>>

    @Query("DELETE FROM order_tables")
    fun deleteAllOrders()

}