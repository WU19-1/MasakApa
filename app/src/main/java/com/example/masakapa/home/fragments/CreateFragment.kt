package com.example.masakapa.home.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.example.masakapa.R
import com.example.masakapa.adapter.IngredientAdapter
import com.example.masakapa.model.Ingredient
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_create.*
import java.security.Permission
import java.util.*
import java.util.jar.Manifest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment() {
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

    var uri: Uri? = null
    var image : CircleImageView? = null
    var add_ingredient : Button? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1001 -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pick_image()
                }else{
                    Toast.makeText(this@CreateFragment.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun pick_image(){
        var i = Intent()
        i.type = "image/*"
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i,1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == 1001){
            image!!.setImageURI(null)
            uri = data?.data
            Log.e("uri",uri.toString())
            image!!.setImageURI(uri)
        }else{
            Log.e("error",resultCode.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_create, container, false)

        image = view.findViewById(R.id.create_image)

        image!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(ActivityCompat.checkSelfPermission(context!!,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ){
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1001)
                }else{
                    pick_image()
                }
            }
        })

        add_ingredient = view.findViewById(R.id.create_add_ingredient)

        var list_view_layout_view = inflater.inflate(R.layout.ingredient_list_view, container,false)
        var list = Vector<Ingredient>()
        list.add(Ingredient("1","Bahan1","kg","0"))
        list.add(Ingredient("1","Bahan2","l","0"))
        list.add(Ingredient("1","Bahan3","lbs","0"))
        list.add(Ingredient("1","Bahan4","cm","0"))
        list.add(Ingredient("1","Bahan5","kg","0"))
        list.add(Ingredient("1","Bahan6","mm","0"))
        list.add(Ingredient("1","Bahan7","gr","0"))
        list.add(Ingredient("1","Bahan8","mg","0"))
        list.add(Ingredient("1","Bahan8","mg","0"))
        list.add(Ingredient("1","Bahan8","mg","0"))
        list.add(Ingredient("1","Bahan8","mg","0"))
        list.add(Ingredient("1","Bahan8","mg","0"))
        list.add(Ingredient("1","Bahan8","mg","0"))
        var container = list_view_layout_view.findViewById<ListView>(R.id.create_ingredient_container)
        var adapter = IngredientAdapter(list,this@CreateFragment.context!!)
        container.adapter = adapter

        var builder = AlertDialog.Builder(this@CreateFragment.activity!!)

        builder.setTitle("Ingredients")

        builder.setView(list_view_layout_view)

        builder.setPositiveButton("Done",object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })

        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.dismiss()
            }
        })

        builder.setView(list_view_layout_view)

        var alert = builder.create()


        add_ingredient!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                alert.show()
                alert.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.or(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM))
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
         * @return A new instance of fragment CreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
