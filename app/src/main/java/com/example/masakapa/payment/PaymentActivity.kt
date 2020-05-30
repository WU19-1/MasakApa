package com.example.masakapa.payment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.example.masakapa.R
import com.example.masakapa.login.Login
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_register.*

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        var t = intent.extras
        var mAuth = FirebaseAuth.getInstance()

        total_payment.text = t!!.getString("price").toString()

        payment_cc_number.maxLines = 1
        payment_cc_number.setSingleLine()
        payment_cc_number.imeOptions = EditorInfo.IME_ACTION_NEXT

        expire_year.maxLines = 1
        expire_year.setSingleLine()
        expire_year.imeOptions = EditorInfo.IME_ACTION_NEXT

        expire_month.maxLines = 1
        expire_month.setSingleLine()
        expire_month.imeOptions = EditorInfo.IME_ACTION_NEXT

        cc_cvv.maxLines = 1
        cc_cvv.setSingleLine()
        cc_cvv.imeOptions = EditorInfo.IME_ACTION_DONE

        submit_payment.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                FirebaseFirestore.getInstance().collection("users")
                    .document(mAuth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener {
                        var user = it.toObject(com.example.masakapa.model.User::class.java)
                        user!!.CardNumber = payment_cc_number.text.toString()
                        FirebaseFirestore.getInstance().collection("users")
                            .document(mAuth.currentUser!!.uid)
                            .set(user).addOnCompleteListener{
                                mAuth.signOut()
                                startActivity(Intent(this@PaymentActivity,Login::class.java))
                                finish()
                            }
                    }
            }
        })
    }
}
