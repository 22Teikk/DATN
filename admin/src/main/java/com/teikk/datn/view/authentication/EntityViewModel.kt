package com.teikk.datn.view.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teikk.datn.data.datasource.remote.EntityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntityViewModel @Inject constructor(
    private val entityRepository: EntityRepository
) : ViewModel() {
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTczMDc3NjIyNSwianRpIjoiNzEyYmEwNTgtOTY3YS00ZTMzLWFkMWMtNGMxZDgzNDM4Zjg5IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6ImhqZ2hqaGdoamdoamdoaiIsIm5iZiI6MTczMDc3NjIyNSwiY3NyZiI6ImVlZDk3MzIzLTQ1NTItNGMxYi1iMGRmLWQwZTg3ZGQwODBmMSIsImV4cCI6MTczMDc3NzEyNX0.GdoABY_3hzB3sPXV2POezSeamwhleIHrxL0YrkwVntY"
    fun getAllEntities() = viewModelScope.launch {
        val headers = mapOf<String, String>(
            "Authentication-Admin" to "teikk",
            "Authorization" to "Bearer $token"
        )
        val data = entityRepository.getAllEntities(headers)
        Log.d("aslkdfjadslkfjalkdsfjaklds", data.toString())
    }
}