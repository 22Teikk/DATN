package com.teikk.datn.data.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teikk.datn.data.model.PaymentMethod

@Dao
interface PaymentMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPaymentMethods(roles: List<PaymentMethod>)

    @Delete
    fun delete(paymentMethod: PaymentMethod)

    @Query("SELECT * FROM payment_method_table")
    fun getAllPaymentMethod(): List<PaymentMethod>
}