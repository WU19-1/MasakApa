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
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class HomeActivity : AppCompatActivity(),
    HomeFragment.OnFragmentInteractionListener,
    HistoryFragment.OnFragmentInteractionListener,
    ForumFragment.OnFragmentInteractionListener,
    CreateFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener
{

    lateinit var mAuth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    var user : User? = null

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun openFragment(fragment : Fragment){
        var t = supportFragmentManager.beginTransaction()
        t.replace(R.id.home_container,fragment)
        t.addToBackStack(null)
        t.commit()
    }

    fun updateUIHome(){
        if(user == null){
            db.collection("users").document(mAuth.currentUser!!.uid).get().addOnSuccessListener{
                user = it.toObject(User::class.java)
                home_name.text = user!!.FullName
                home_status.text = user!!.Status
            }.addOnFailureListener(object : OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Log.e("firestore error", p0.message.toString())
                    Toast.makeText(this@HomeActivity,p0.message,Toast.LENGTH_LONG).show()
                }
            })
        }else{
            home_name.text = user!!.FullName
            home_status.text = user!!.Status
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        openFragment(HomeFragment.newInstance("",""))

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        updateUIHome()

        bottom_navigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.navigation_home -> {
                        openFragment(HomeFragment.newInstance("",""))
                        updateUIHome()
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
