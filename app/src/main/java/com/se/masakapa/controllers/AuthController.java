package com.se.masakapa.controllers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.se.masakapa.listeners.ListenerAdapter;
import com.se.masakapa.repositories.UserRepository;


public class AuthController {

    public static void Register(final String email, String password, final String displayName, final String profilePicture, final ListenerAdapter listener){
        // Validation here
        UserRepository.Register(email, password, new ListenerAdapter() {
            @Override
            public void newUserRegistered(FirebaseUser u) {
                listener.newItemInserted(UserRepository.InsertUser(u.getUid(), email, displayName, profilePicture));
            }
        });

    }
    public static Task<AuthResult> Login(String email, String password){
        return UserRepository.Login(email, password);
    }

    public static void Logout(){
        FirebaseAuth.getInstance().signOut();
    }
}
