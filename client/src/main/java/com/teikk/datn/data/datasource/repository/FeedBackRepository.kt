package com.teikk.datn.data.datasource.repository

import com.teikk.datn.data.model.Feedback
import com.teikk.datn.data.service.ApiService
import javax.inject.Inject

class FeedBackRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun makeFeedBack(feedback: Feedback) = apiService.makeFeedback(feedback)
}