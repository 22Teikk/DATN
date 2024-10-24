package com.teikk.datn.view.authentication

import androidx.lifecycle.ViewModel
import com.teikk.datn.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun signUpWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        authenticationRepository.signUpWithEmail(email, password) {
            callback(it)
        }
    }

    fun signInWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        authenticationRepository.signInWithEmail(email, password) {
            callback(it)
        }
    }

}