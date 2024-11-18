package com.teikk.datn.data.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teikk.datn.data.datasource.local.PaymentMethodLocalRepository
import com.teikk.datn.data.datasource.remote.PaymentMethodRemoteRepository
import com.teikk.datn.data.model.PaymentMethod
import javax.inject.Inject

class PaymentMethodRepository @Inject constructor(
    private val paymentMethodRemoteRepository: PaymentMethodRemoteRepository,
    private val paymentMethodLocalRepository: PaymentMethodLocalRepository,
){
    private val _paymentMethodsLiveData = MutableLiveData<List<PaymentMethod>>()
    val paymentMethodsLiveData: LiveData<List<PaymentMethod>> get() = _paymentMethodsLiveData
    suspend fun fetchPaymentMethodData()  {
        val response = paymentMethodRemoteRepository.getAllPaymentMethods()
        if (response.isSuccessful) {
            val paymentMethods = response.body()!!
            paymentMethods[0].isSelected = true
            paymentMethodLocalRepository.insertPaymentMethods(paymentMethods)
            _paymentMethodsLiveData.postValue(paymentMethods)
        }
    }
}