package com.example.masakapa.home.fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.masakapa.R
import com.example.masakapa.adapter.ExtendableAdapter
import com.example.masakapa.model.Favorite
import com.example.masakapa.model.History
import com.example.masakapa.model.Ingredient
import com.example.masakapa.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HistoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
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
    var vector : Vector<Recipe> = Vector()
    lateinit var adapter : ExtendableAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_history, container, false)

        var title = view.findViewById<TextView>(R.id.extends_title)


        var db = FirebaseFirestore.getInstance().collection("recipes")

        var extend_container = view.findViewById<RecyclerView>(R.id.history_container)
        extend_container.layoutManager = LinearLayoutManager(this@HistoryFragment.context)

        adapter = ExtendableAdapter(vector,this@HistoryFragment.activity!!,this@HistoryFragment.activity!!.windowManager)
        extend_container.adapter  = adapter

        var b = arguments
        var found = true
        if(b != null){
            if(b.getString("tags").equals("Recipe")){
                title.text = "Recipe"
                db.get().addOnSuccessListener{
                    for (i in it){
                        vector.add(i.toObject(Recipe::class.java))
                    }
                    adapter.notifyDataSetChanged()
                }
            }else if(b.getString("tags").equals("Favorite")){
                //tampilin favorit
                title.text = "Favorite"
                FirebaseFirestore.getInstance().collection("favorite").document(
                    FirebaseAuth.getInstance().currentUser!!.uid
                ).get().addOnSuccessListener {
                    if(it.exists()){
                        var temp = it.toObject(Favorite::class.java)
                        if(temp!!.fav != null){
                            for(i in temp!!.fav!!.iterator()){
                                var r : Recipe? = null
                                db.document(i).get().addOnSuccessListener{
                                    r = it.toObject(Recipe::class.java)
                                    vector.add(r!!)
                                    adapter.notifyDataSetChanged()
                                }
                            }

                        }
                    }
                }
            }else if(b.getString("tags").equals("Search")){
                title.text = "Search"
                db.get().addOnSuccessListener {
                    for (i in it){
                        found = true
                        var curr = i.toObject(Recipe::class.java)
                        var s = b.getString("result")!!.split(",")
                        for(i in s){
                            var temp = false
                            for(q in curr.Ingredients!!){
                                if(q.value.Name!!.equals(i)){
                                    temp = true
                                    break
                                }
                            }
                            if(!temp && found){
                                found = false
                                break
                            }
                        }
                        if(found){
                            vector.add(curr)
                        }
                    }
                    vector.forEach {
                        Log.e("vector",it.RecipeName)
                    }
                    adapter = ExtendableAdapter(vector,this@HistoryFragment.activity!!,this@HistoryFragment.activity!!.windowManager)
                    extend_container.adapter  = adapter
                }
            }else if(b.getString("tags").equals("History")){
                //tampilin history biasa
                Log.e("masuk","true")
                title.text = "History"
                FirebaseFirestore.getInstance().collection("history").document(
                    FirebaseAuth.getInstance().currentUser!!.uid
                ).get().addOnSuccessListener {
                    Log.e("masuk","true")
                    var h = it.toObject(History::class.java)
                    Log.e(h.toString(),"exists")
                    for(i in h!!.history!!){
                        var r : Recipe? = null
                        db.document(i.key).get().addOnSuccessListener{
                            r = it.toObject(Recipe::class.java)
                            Log.e("r",r!!.RecipeID)
                            vector.add(r!!)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
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
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
