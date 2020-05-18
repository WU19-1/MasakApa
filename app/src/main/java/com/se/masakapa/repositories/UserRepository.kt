package com.se.masakapa.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.se.masakapa.listeners.Listeners
import com.se.masakapa.models.User

object UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    fun insertUser(uid: String?, email: String?, displayName: String?, profilePicture: String?): Task<DocumentReference> {
        val u = User(uid, email!!, displayName!!, profilePicture!!)
        return db.collection("users")
                .add(u)
    }

    fun register(email: String?, password: String?, listener: Listeners): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnSuccessListener { authResult -> listener.newUserRegistered(authResult.user) }
    }

    fun login(email: String?, password: String?): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email!!, password!!)
    }
}