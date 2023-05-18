package com.geekymusketeers.medify.ui.auth.signUpScreen

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class SignUpRepository {

    suspend fun registerUser(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: FirebaseAuthUserCollisionException) {
            null // Return null if the user email already exists
        } catch (e: Exception) {
            null
        }
    }

}