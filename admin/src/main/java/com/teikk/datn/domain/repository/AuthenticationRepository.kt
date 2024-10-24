package com.teikk.datn.domain.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun getCurrentUser() = auth.currentUser
    fun checkAuth(): Boolean = auth.currentUser != null

    fun signInWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        var isSuccess = false
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }

    fun signUpWithEmail(email: String, password: String, callback: (Boolean) -> Unit) {
        var isSuccess = false
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }
    fun signOut() = auth.signOut()
}