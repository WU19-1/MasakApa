package com.example.masakapa.home

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.masakapa.R
import com.example.masakapa.home.fragments.*
import com.example.masakapa.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeActivity : AppCompatActivity(),
    HomeFragment.OnFragmentInteractionListener,
    HistoryFragment.OnFragmentInteractionListener,
    ForumFragment.OnFragmentInteractionListener,
    CreateFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener
{

    lateinit var mAuth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun openFragment(fragment : Fragment){
        var t = supportFragmentManager.beginTransaction()
        t.replace(R.id.home_container,fragment)
        t.addToBackStack(null)
        t.commit()
    }

    fun updateUI(){
        var user : User? = null
        db.collection("users").document(mAuth.currentUser!!.uid).get().addOnSuccessListener {
            if(user == null){
                user = it.toObject(User::class.java)
            }
            home_name.text = user!!.FullName
            home_status.text = user!!.Status
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        home_container.layoutParams.width = applicationContext.resources.displayMetrics.heightPixels - bottom_navigation.layoutParams.height

        bottom_navigation.selectedItemId = R.id.navigation_home

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        bottom_navigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.navigation_home -> {
                        openFragment(HomeFragment.newInstance("",""))
                        updateUI()
                        return true
                    }
                    R.id.navigation_forum -> {
                        openFragment(ForumFragment.newInstance("",""))
                        return true
                    }
                    R.id.navigation_create -> {
                        openFragment(CreateFragment.newInstance("",""))
                        return true
                    }
                    R.id.navigation_history -> {
                        openFragment(HistoryFragment.newInstance("",""))
                        return true
                    }
                    R.id.navigation_profile -> {
                        openFragment(ProfileFragment.newInstance("",""))
                        return true
                    }
                }
                return false
            }

        })
    }
}
