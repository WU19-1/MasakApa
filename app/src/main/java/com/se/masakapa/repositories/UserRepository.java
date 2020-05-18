package com.se.masakapa.repositories;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se.masakapa.listeners.Listeners;
import com.se.masakapa.models.User;

public class UserRepository {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static Task<DocumentReference> InsertUser(final String uid, final String email, final String displayName, final String profilePicture){
        User u = new User(uid, email, displayName, profilePicture);
        return db.collection("users")
                .add(u);

    }
    public static Task<AuthResult> Register(String email, String password, final Listeners listener){
        return auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        listener.newUserRegistered(authResult.getUser());
                    }
                });
    }

    public static Task<AuthResult> Login(String email, String password){
        return auth.signInWithEmailAndPassword(email,password);
    }

}
