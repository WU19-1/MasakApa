package com.se.masakapa.listeners

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference

abstract class ListenerAdapter : Listeners {
    override fun newItemInserted(task: Task<DocumentReference>) {}
    override fun newUserRegistered(u: FirebaseUser) {}
}