package com.se.masakapa.listeners

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference

interface Listeners {
    fun newUserRegistered(u: FirebaseUser?)
    fun newItemInserted(task: Task<DocumentReference?>?)
}