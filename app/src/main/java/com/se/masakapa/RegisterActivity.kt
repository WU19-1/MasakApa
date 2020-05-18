package com.se.masakapa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.se.masakapa.controllers.AuthController
import com.se.masakapa.listeners.Listeners
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_button.setOnClickListener {
            register_button.visibility = (View.GONE)
            register_progress.visibility = (View.VISIBLE)
            AuthController.register(email.text.toString(), password.text.toString(), display_name.text.toString(), "", object : Listeners {
                override fun newItemInserted(task: Task<DocumentReference>) {
                    task.addOnSuccessListener {
                        Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                    }
                            .addOnFailureListener { e -> Log.wtf("Failure", e) }
                }
            })
        }
    }
}