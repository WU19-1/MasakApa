package com.example.masakapa.home

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.masakapa.R
import com.example.masakapa.home.fragments.*
import com.example.masakapa.model.Ingredient
import com.example.masakapa.model.Recipe
import com.example.masakapa.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.util.*

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

    fun openFragment(fragment : Fragment,tag : String){
        var t = supportFragmentManager.beginTransaction()
        var b = Bundle()
        b.putString("tags",tag)
        fragment.arguments = b
        t.replace(R.id.home_container,fragment)
        t.addToBackStack(null)
        t.commit()
    }

    fun updateUIHome(){
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun insert(){
        var dbtest = FirebaseFirestore.getInstance().collection("ingredients")

        var list: Vector<Ingredient> = Vector()
        list.add(Ingredient("","acar","gr","0"))
        list.add(Ingredient("","air","ml","0"))
        list.add(Ingredient("","air lemon","ml","0"))
        list.add(Ingredient("","asam jawa","potong","0"))
        list.add(Ingredient("","ayam","gr","0"))
        list.add(Ingredient("","bawang goreng","gr","0"))
        list.add(Ingredient("","bawang merah","gr","0"))
        list.add(Ingredient("","bawang putih","gr","0"))
        list.add(Ingredient("","bubuk kari","sdm","0"))
        list.add(Ingredient("","cabe merah","gr","0"))
        list.add(Ingredient("","cabe rawit","gr","0"))
        list.add(Ingredient("","daun jeruk","gr","0"))
        list.add(Ingredient("","daun salam","gr","0"))
        list.add(Ingredient("","ebi","gr","0"))
        list.add(Ingredient("","garam","sdm","0"))
        list.add(Ingredient("","gula","sdm","0"))
        list.add(Ingredient("","gula merah","sdm","0"))
        list.add(Ingredient("","ikan tengiri","gr","0"))
        list.add(Ingredient("","jahe","gr","0"))
        list.add(Ingredient("","jeruk nipis","gr","0"))
        list.add(Ingredient("","kacang tanah","gr","0"))
        list.add(Ingredient("","kaldu ayam","sdm","0"))
        list.add(Ingredient("","kecap asin","ml","0"))
        list.add(Ingredient("","kecap manis","ml","0"))
        list.add(Ingredient("","kemiri","gr","0"))
        list.add(Ingredient("","kerupuk","gr","0"))
        list.add(Ingredient("","ketumbar","gr","0"))
        list.add(Ingredient("","kol","gr","0"))
        list.add(Ingredient("","lengkuas","gr","0"))
        list.add(Ingredient("","lontong ","gr","0"))
        list.add(Ingredient("","mie","gr","0"))
        list.add(Ingredient("","minyak goreng","ml","0"))
        list.add(Ingredient("","msg","sdm","0"))
        list.add(Ingredient("","nangka muda","gr","0"))
        list.add(Ingredient("","santan","ml","0"))
        list.add(Ingredient("","sawi","gr","0"))
        list.add(Ingredient("","selada","gr","0"))
        list.add(Ingredient("","seledri","gr","0"))
        list.add(Ingredient("","sereh","gr","0"))
        list.add(Ingredient("","tahu","gr","0"))
        list.add(Ingredient("","teh","sdm","0"))
        list.add(Ingredient("","telur","gr","0"))
        list.add(Ingredient("","tempe","gr","0"))
        list.add(Ingredient("","tepung tapioka","gr","0"))
        list.add(Ingredient("","tepung terigu","gr","0"))
        list.add(Ingredient("","terasi","sdm","0"))
        list.add(Ingredient("","timun","gr","0"))
        list.add(Ingredient("","tomat","gr","0"))
        list.add(Ingredient("","wortel","gr","0"))
        for(i in 0..list.size-1){
            dbtest.add(list.get(i))
        }

        FirebaseFirestore.getInstance().collection("ingredients").get().addOnSuccessListener{
            for (i in it){
                var u = i.toObject(Ingredient::class.java)
                u.IngredientID = i.id
                FirebaseFirestore.getInstance().collection("ingredients").document(i.id).set(u)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        openFragment(HomeFragment.newInstance("",""),"Home")

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        bottom_navigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.navigation_home -> {
                        openFragment(HomeFragment.newInstance("",""),"Home")
                        return true
                    }
                    R.id.navigation_forum -> {
                        openFragment(ForumFragment.newInstance("",""),"Forum")
                        return true
                    }
                    R.id.navigation_create -> {
                        openFragment(CreateFragment.newInstance("",""),"Create")
                        return true
                    }
                    R.id.navigation_history -> {
                        openFragment(HistoryFragment.newInstance("",""),"History")
                        return true
                    }
                    R.id.navigation_profile -> {
                        openFragment(ProfileFragment.newInstance("",""),"Profile")
                        return true
                    }
                }
                return false
            }
        })
    }
}
