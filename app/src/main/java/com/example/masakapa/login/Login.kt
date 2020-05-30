package com.example.masakapa.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.masakapa.R
import com.example.masakapa.forgotpw.ForgotPassword
import com.example.masakapa.home.HomeActivity
import com.example.masakapa.register.Register
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        login_email.maxLines = 1
        login_email.setSingleLine()
        login_email.imeOptions = EditorInfo.IME_ACTION_NEXT

        login_pw.imeOptions = EditorInfo.IME_ACTION_DONE

        forgot_password.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(this@Login,ForgotPassword::class.java))
            }
        })

        login_login_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(login_email.text.toString().isEmpty()){
                    Toast.makeText(this@Login,"Email cannot be empty",Toast.LENGTH_LONG).show()
                }else if(login_pw.text.toString().isEmpty()){
                    Toast.makeText(this@Login,"Password cannot be empty",Toast.LENGTH_LONG).show()
                }else{
                    mAuth.signInWithEmailAndPassword(login_email.text.toString(),login_pw.text.toString())
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                //pergi ke home
                                var i = Intent(this@Login, HomeActivity::class.java)
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(i)
                            }else{
                                Toast.makeText(this@Login,"Invalid username and password",Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        })

        login_register_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i = Intent(this@Login,Register::class.java)
                startActivity(i)
            }
        })
    }
}
