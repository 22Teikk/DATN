package com.teikk.datn.data.datasource.local

import com.teikk.datn.data.datasource.service.dao.PaymentMethodDao
import com.teikk.datn.data.model.PaymentMethod
import javax.inject.Inject

class PaymentMethodLocalRepository @Inject constructor(
    private val paymentMethodDao: PaymentMethodDao
) {
    suspend fun getAllPaymentMethods() = paymentMethodDao.getAllPaymentMethod()
    suspend fun insertPaymentMethods(paymentMethods: List<PaymentMethod>) = paymentMethodDao.insertPaymentMethods(paymentMethods)
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethod) = paymentMethodDao.delete(paymentMethod)
}