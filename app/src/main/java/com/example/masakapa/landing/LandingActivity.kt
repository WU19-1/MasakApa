package com.example.masakapa.landing

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.example.masakapa.R
import com.example.masakapa.adapter.LandingAdapter
import com.example.masakapa.home.HomeActivity
import com.example.masakapa.login.Login
import com.example.masakapa.model.User
import com.example.masakapa.register.Register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : AppCompatActivity(),
    FirstLanding.OnFragmentInteractionListener,
    SecondLanding.OnFragmentInteractionListener,
    ThirdLanding.OnFragmentInteractionListener
    {
        override fun onFragmentInteraction(uri: Uri) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        var mAuth = FirebaseAuth.getInstance().currentUser
        if(mAuth != null){
            var i = Intent(this@LandingActivity, HomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            finish()
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        landing_pager.adapter = LandingAdapter(supportFragmentManager)

        landing_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        dot1.setTextColor(Color.WHITE)
                        dot2.setTextColor(Color.parseColor("#70000000"))
                        dot3.setTextColor(Color.parseColor("#70000000"))
                    }
                    1 -> {
                        dot1.setTextColor(Color.parseColor("#70000000"))
                        dot2.setTextColor(Color.WHITE)
                        dot3.setTextColor(Color.parseColor("#70000000"))
                    }
                    2 -> {
                        dot1.setTextColor(Color.parseColor("#70000000"))
                        dot2.setTextColor(Color.parseColor("#70000000"))
                        dot3.setTextColor(Color.WHITE)
                    }
                }
            }

        })

        login_landing.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i = Intent(applicationContext, Login::class.java)
                startActivity(i)
            }
        })

        sign_in_with_google.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

            }
        })

        sign_up_landing.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i = Intent(applicationContext, Register::class.java)
                startActivity(i)
            }
        })
    }
}
