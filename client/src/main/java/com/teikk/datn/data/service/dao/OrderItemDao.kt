package com.teikk.datn.data.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.teikk.datn.data.model.Order
import com.teikk.datn.data.model.OrderItem

@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderItem(order: List<OrderItem>)

    @Delete
    fun delete(order: OrderItem)

    @Query("SELECT * FROM order_item_tables")
    fun getAllOrderItem(): Flow<List<OrderItem>>

    @Query("DELETE FROM order_item_tables")
    fun deleteAllOrderItems()

}