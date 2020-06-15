package com.example.masakapa.home.fragments

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
import android.widget.LinearLayout

import com.example.masakapa.R
import com.example.masakapa.landing.LandingActivity
import com.example.masakapa.model.User
import com.example.masakapa.profile.EditProfile
import com.example.masakapa.subscription.Subscription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
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

    var user : User? = null
    lateinit var mAuth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    fun updateUIProfile(){
        if(user == null){
            db.collection("users").document(mAuth.currentUser!!.uid).get().addOnSuccessListener{
                user = it.toObject(User::class.java)
                profile_name.text = user!!.FullName
                profile_status.text = user!!.Status
                profile_email.text = user!!.Email
                profile_image.setImageBitmap(null)
                if(!user!!.Image!!.isEmpty()){
                    FirebaseStorage.getInstance().reference.child(user!!.Image!!).getBytes(8096*8096)
                        .addOnSuccessListener {
                            if(it.isNotEmpty()){
                                var bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                                profile_image.setImageBitmap(bitmap)
                            }
                        }
                }
            }
        }else{
            profile_name.text = user!!.FullName
            profile_status.text = user!!.Status
            profile_email.text = user!!.Email
            if(!user!!.Image!!.isEmpty()){
                FirebaseStorage.getInstance().reference.child(user!!.Image!!).getBytes(8096*8096)
                    .addOnSuccessListener {
                        if(it.isNotEmpty()){
                            var bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                            profile_image.setImageBitmap(bitmap)
                        }
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        updateUIProfile()

        view.findViewById<LinearLayout>(R.id.profile_logout).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                FirebaseAuth.getInstance().signOut()
                activity!!.finish()
                startActivity(Intent(activity,LandingActivity::class.java))
            }
        })

        view.findViewById<LinearLayout>(R.id.profile_membership).setOnClickListener{
            startActivity(Intent(activity,Subscription::class.java))
        }

        view.findViewById<LinearLayout>(R.id.profile_edit_profile).setOnClickListener{
            startActivity(Intent(activity,EditProfile::class.java))
        }


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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
