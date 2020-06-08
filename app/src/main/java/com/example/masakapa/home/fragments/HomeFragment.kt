package com.example.masakapa.home.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView

import com.example.masakapa.R
import com.example.masakapa.model.Recipe
import com.example.masakapa.model.User
import com.example.masakapa.recipe.RecipeDetail
import com.example.masakapa.search.SearchActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
import org.w3c.dom.Text
import java.lang.Exception
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var db = FirebaseFirestore.getInstance()
    var mAuth = FirebaseAuth.getInstance()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && data != null){
            Log.e("masuk","true")
            var t = this@HomeFragment.activity!!.supportFragmentManager.beginTransaction()
            var frag = HistoryFragment.newInstance("","")
            var b = Bundle()
            b.putString("tags","Search")
            b.putString("result",data.extras!!.getString("result"))
            frag.arguments = b
            t.replace(R.id.home_container,frag)
            t.addToBackStack(null)
            t.commit()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        var name = view.findViewById<TextView>(R.id.home_name)
        var desc = view.findViewById<TextView>(R.id.home_status)

        var user : User? = null

        db.collection("users").document(mAuth.currentUser!!.uid).get().addOnSuccessListener{
            user = it.toObject(User::class.java)
            home_name.text = user!!.FullName
            home_status.text = user!!.Status
        }.addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {
                Log.e("firestore error", p0.message.toString())
                Toast.makeText(this@HomeFragment.context,p0.message, Toast.LENGTH_LONG).show()
            }
        })


        var racik = view.findViewById<LinearLayout>(R.id.home_racik)

        var activity_view = this.activity!!.findViewById<FrameLayout>(R.id.home_container)

        racik.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivityForResult(Intent(this@HomeFragment.context,SearchActivity::class.java),1)
            }
        })

        var recipe = view.findViewById<LinearLayout>(R.id.home_resep)

        var fav = view.findViewById<LinearLayout>(R.id.home_favorit)

        var rec_con = view.findViewById<CardView>(R.id.home_recommend_container)

        var title = view.findViewById<TextView>(R.id.home_recommend_title)

        var imave = view.findViewById<ImageView>(R.id.home_recommend_image)

        FirebaseFirestore.getInstance().collection("recipes").get().addOnSuccessListener{
            var v = Vector<Recipe>()
            for (i in it){
                v.add(i.toObject(Recipe::class.java))
            }
            var r = v.get((Math.random()*v.size).toInt())
            title.text = r.RecipeName
            FirebaseStorage.getInstance().reference.child(r.RecipeImage!!).getBytes(8096*8096)
                .addOnSuccessListener {
                    var b = BitmapFactory.decodeByteArray(it,0,it.size)
                    imave.setImageBitmap(b)
                }
            rec_con.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    var i = Intent(this@HomeFragment.context, RecipeDetail::class.java)
                    i.putExtra("rid",r.RecipeID)
                    startActivity(i)
                }

            })
        }

        fav.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var t = this@HomeFragment.activity!!.supportFragmentManager.beginTransaction()
                var frag = HistoryFragment.newInstance("","")
                var b = Bundle()
                b.putString("tags","Favorite")
                frag.arguments = b
                t.replace(R.id.home_container,frag)
                t.addToBackStack(null)
                t.commit()
            }

        })

        recipe.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var t = this@HomeFragment.activity!!.supportFragmentManager.beginTransaction()
                var frag = HistoryFragment.newInstance("","")
                var b = Bundle()
                b.putString("tags","Recipe")
                frag.arguments = b
                t.replace(R.id.home_container,frag)
                t.addToBackStack(null)
                t.commit()
            }
        })

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
