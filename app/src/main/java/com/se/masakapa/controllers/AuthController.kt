package com.se.masakapa.controllers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.se.masakapa.listeners.Listeners
import com.se.masakapa.repositories.UserRepository

object AuthController {
    fun register(email: String?, password: String?, displayName: String?, profilePicture: String?, listener: Listeners) {
        // Validation here
        UserRepository.register(email, password, object : Listeners {
            override fun newUserRegistered(u: FirebaseUser) {
                listener.newItemInserted(UserRepository.insertUser(u.uid, email, displayName, profilePicture))
            }
        })
    }

    fun login(email: String?, password: String?): Task<AuthResult> {
        return UserRepository.login(email, password)
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}