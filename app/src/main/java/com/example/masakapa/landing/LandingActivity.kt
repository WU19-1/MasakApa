package com.example.masakapa.landing

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.masakapa.R
import com.example.masakapa.adapter.LandingAdapter
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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        sign_in_with_google.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        sign_up_landing.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
