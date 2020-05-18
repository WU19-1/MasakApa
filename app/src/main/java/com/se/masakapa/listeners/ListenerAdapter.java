package com.se.masakapa.listeners;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public abstract class ListenerAdapter implements Listeners {
    @Override
    public void newItemInserted(Task<DocumentReference> task) {

    }

    @Override
    public void newUserRegistered(FirebaseUser u) {

    }
}
