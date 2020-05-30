package com.example.masakapa.register

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.masakapa.R
import com.example.masakapa.login.Login
import com.example.masakapa.model.User
import com.example.masakapa.subscription.Subscription
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class Register : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    var db = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var errors = listOf<TextView>(register_first_name_error,register_password_error,register_email_error,register_confirm_password_error)
        var edits = listOf<EditText>(register_password,register_email,register_date,register_confirm_password,register_first_name)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        register_email.maxLines = 1
        register_email.setSingleLine()
        register_email.imeOptions = EditorInfo.IME_ACTION_NEXT

        register_first_name.maxLines = 1
        register_first_name.setSingleLine()
        register_first_name.imeOptions = EditorInfo.IME_ACTION_NEXT

        register_password.imeOptions = EditorInfo.IME_ACTION_NEXT
        register_confirm_password.imeOptions = EditorInfo.IME_ACTION_NEXT

        register_date.imeOptions = EditorInfo.IME_ACTION_DONE

        mAuth = FirebaseAuth.getInstance()

        var id_error = R.drawable.ic_error_red_24dp

        val calendar = Calendar.getInstance()

        var date = DatePickerDialog.OnDateSetListener(object : DatePickerDialog.OnDateSetListener,
                (DatePicker, Int, Int, Int) -> Unit {
            override fun invoke(p1: DatePicker, p2: Int, p3: Int, p4: Int) {
                calendar.set(Calendar.YEAR,p2)
                calendar.set(Calendar.MONTH,p3)
                calendar.set(Calendar.DAY_OF_MONTH,p4)
                val myFormat = "dd/MM/yyyy"
                Log.e("dates",calendar.toString())
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                register_date.setText(sdf.format(calendar.time))
            }

            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                calendar.set(Calendar.YEAR,p1)
                calendar.set(Calendar.MONTH,p2)
                calendar.set(Calendar.DAY_OF_MONTH,p3)
                val myFormat = "MM/dd/yy"
                Log.e("dates",calendar.toString())
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                register_date.setText(sdf.format(calendar.time))
            }
        })

        register_date.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if(p1){
                    DatePickerDialog(this@Register, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }

        register_date.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                DatePickerDialog(this@Register, date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        have_account.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Register,Login::class.java))
        })

        register_email.setOnClickListener({
            register_email.requestFocus()
        })

        register_continue.setOnClickListener({
            errors.forEach {
                it.visibility = View.INVISIBLE
            }
            edits.forEach {
                it.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
            }
            if(register_first_name.text.isEmpty()){
                register_first_name.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                register_first_name_error.text = "First name cannot be empty"
                register_first_name_error.visibility = View.VISIBLE
            }

            if(register_password.text.isEmpty()){
                register_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                register_password_error.text = "Password field cannot be empty"
                register_password_error.visibility = View.VISIBLE
            }
            else if(register_password.length() < 8 || register_password.length() > 24){
                register_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                register_password_error.text = "Password must have a length of 8 to 24 characters"
                register_password_error.visibility = View.VISIBLE
            }

            if(register_confirm_password.text.isEmpty()){
                register_confirm_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                register_confirm_password_error.text = "Confirm password cannot be empty"
                register_confirm_password_error.visibility = View.VISIBLE
            }else if(!register_confirm_password.text.toString().equals(register_password.text.toString())){
                register_confirm_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                register_confirm_password_error.text = "Confirm password must be the same as Password field"
                register_confirm_password_error.visibility = View.VISIBLE
            }

            try {
                var split_at = register_email.text.split("@")
                if(register_email.text.isEmpty()){
                    register_email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                    register_email_error.text = "Email cannot be empty"
                    register_email_error.visibility = View.VISIBLE
                }else if(!register_email.text.endsWith(".com") && !register_email.text.endsWith(".co.id")){
                    register_email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                    register_email_error.text = "Email must ends with .com or .co.id"
                    register_email_error.visibility = View.VISIBLE
                }else if(register_email.text.startsWith("@") || register_email.text.endsWith("@")){
                    register_email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                    register_email_error.text = "Email can't starts with or ends with @"
                    register_email_error.visibility = View.VISIBLE
                }else if(split_at[0].endsWith(".") || split_at[1].startsWith(".")){
                    register_email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                    register_email_error.text = ". (dot) cannot be placed next to @"
                    register_email_error.visibility = View.VISIBLE
                }else if(split_at.size > 2){
                    register_email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                    register_email_error.text = "@ can only be one"
                    register_email_error.visibility = View.VISIBLE
                }
            }catch (e : Exception){
                register_email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, id_error, 0)
                register_email_error.text = "Email must contain @"
                register_email_error.visibility = View.VISIBLE
            }

            var isError = false
            for(e in errors){
                if(e.visibility == View.VISIBLE){
                    isError = true
                    break
                }
            }
            if(!isError){
                //register kuy
                var email = register_email.text.toString()
                var password = register_password.text.toString()
                var full_name = register_first_name.text.toString()
                var dob = register_date.text.toString()
                var id = ""
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener({
                        if(it.isSuccessful){
                            //daftarin info ke database
                            id = mAuth.currentUser!!.uid
                            var u = User()
                            u.UserID = id
                            u.Email = email
                            u.DOB = dob
                            u.FullName = full_name
                            u.Subscription = "Normal"
                            u.Status = "I am new to Masak Apa!"
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(id)
                                .set(
                                    hashMapOf(
                                        "UserID" to u.UserID,
                                        "Email" to u.Email,
                                        "FullName" to u.FullName,
                                        "DOB" to u.DOB,
                                        "Subscription" to u.Subscription,
                                        "CardNumber" to u.CardNumber,
                                        "Status" to u.Status
                                    ) as Map<String, Any>
                                ).addOnCompleteListener{
                                    finish()
                                    startActivity(Intent(this@Register,Subscription::class.java))
                                }
                        }else{
                            Toast.makeText(this@Register,it.exception!!.message,Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        })
    }
}
