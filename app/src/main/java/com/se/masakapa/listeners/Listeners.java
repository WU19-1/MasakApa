package com.se.masakapa.listeners;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public interface Listeners {
    void newUserRegistered(FirebaseUser u);
    void newItemInserted(Task<DocumentReference> task);
}
