package com.geekymusketeers.medify.ui.auth.signInScreen

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class SignInRepository {
    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: FirebaseAuthInvalidUserException) {
            null // Return null if the user is not registered
        } catch (e: Exception) {
            null
        }
    }
}