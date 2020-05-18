package com.se.masakapa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.se.masakapa.controllers.AuthController;
import com.se.masakapa.listeners.ListenerAdapter;

public class RegisterActivity extends AppCompatActivity {
    ProgressBar pb;
    Button registerButton;
    EditText m_email;
    EditText m_displayName;
    EditText m_password;
    EditText m_confirmationPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.register_button);
        pb = findViewById(R.id.register_progress);
        m_email = findViewById(R.id.email);
        m_displayName = findViewById(R.id.display_name);
        m_password = findViewById(R.id.password);
        m_confirmationPassword = findViewById(R.id.confirmation_password);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                registerButton.setVisibility(View.GONE);
//                pb.setVisibility(View.VISIBLE);
                AuthController.Register(m_email.getText().toString(), m_password.getText().toString(), m_displayName.getText().toString(), "", new ListenerAdapter() {
                    @Override
                    public void newItemInserted(Task<DocumentReference> task){
                        task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.wtf("Failure", e);
                            }
                        });
                    }
                });

            }
        });
    }
}
