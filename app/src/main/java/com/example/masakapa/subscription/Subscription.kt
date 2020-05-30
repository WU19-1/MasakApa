package com.example.masakapa.subscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.masakapa.R
import com.example.masakapa.login.Login
import com.example.masakapa.payment.PaymentActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_subscription.*

class Subscription : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        try_it.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@Subscription,Login::class.java))
                finish()
            }
        })

        subscribe_now.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i = Intent(this@Subscription,PaymentActivity::class.java)
                var b = Bundle()
                b.putString("price","Rp. 10.000")
                i.putExtras(b)
                startActivity(i)
                finish()
            }
        })
    }
}
