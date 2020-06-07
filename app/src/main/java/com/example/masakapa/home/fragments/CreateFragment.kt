package com.example.masakapa.home.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

import com.example.masakapa.R
import com.example.masakapa.adapter.IngredientAdapter
import com.example.masakapa.model.Ingredient
import com.example.masakapa.model.Recipe
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_create.*
import java.security.Permission
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
    var create : Button? = null

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

    lateinit var step : EditText
    final var ispressed = arrayOf(false)

    fun onKeyDown(){
        step.clearFocus()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_create, container, false)

        var db = FirebaseFirestore.getInstance()
            .collection("recipes")

        var name = view.findViewById<EditText>(R.id.create_ingredient_recipe_name)

        var desc = view.findViewById<EditText>(R.id.create_desc)

        var serving = view.findViewById<TextInputEditText>(R.id.create_serving)

        var prep = view.findViewById<TextInputEditText>(R.id.create_prep)

        var cook = view.findViewById<TextInputEditText>(R.id.create_cook)

        create = view.findViewById<Button>(R.id.create_recipe)
        create!!.isFocusable = true

        step = view.findViewById<EditText>(R.id.create_step)

        step.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == KeyEvent.KEYCODE_BACK){
                    var imm = getActivity()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(getView()!!.getWindowToken(), 0);
                    step.clearFocus()
                    return true
                }
                return false
            }

        })

        name.imeOptions = EditorInfo.IME_ACTION_NEXT
        desc.imeOptions = EditorInfo.IME_ACTION_NEXT
        serving.imeOptions = EditorInfo.IME_ACTION_NEXT
        serving.setSingleLine()
        serving.maxLines = 1
        prep.imeOptions = EditorInfo.IME_ACTION_NEXT
        prep.setSingleLine()
        prep.maxLines = 1
        cook.imeOptions = EditorInfo.IME_ACTION_DONE
        cook.setSingleLine()
        cook.maxLines = 1

        step.setScroller(Scroller(this.context))
        step.isVerticalScrollBarEnabled = true
        step.movementMethod = ScrollingMovementMethod()

        var all_cont = arrayListOf<View>(
            view.findViewById(R.id.create_ingredient_container_frag),
            view.findViewById(R.id.create_ingredient_layout_container),
            view.findViewById(R.id.create_desc_container)
        )

        step.isFocusable = true

        var listener = object : View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if(p1){
                    all_cont.forEach { it.visibility = View.GONE }
                }else{
                    all_cont.forEach { it.visibility = View.VISIBLE }
                }
            }
        }

        step.setOnFocusChangeListener(listener)


        step.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if(p2!!.action == KeyEvent.ACTION_DOWN && p1 == 66){
                    if(ispressed[0]){
                        step.text.append("\n")
                        ispressed[0] = false
                        return true
                    }else{
                        step.clearFocus()
                        var imm = getActivity()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(getView()!!.getWindowToken(), 0);
                        return true
                    }
                }
                else if(p2!!.action == KeyEvent.ACTION_DOWN && p1 == 59){
                    ispressed[0] = true
                }
                return false
            }
        })

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
        var adapter = IngredientAdapter(list,this@CreateFragment.context!!)
        var container = list_view_layout_view.findViewById<ListView>(R.id.create_ingredient_container)
        container.adapter = adapter

        FirebaseFirestore.getInstance().collection("ingredients").get().addOnSuccessListener {
            for(q in it){
                list.add(q.toObject(Ingredient::class.java))
            }
            adapter = IngredientAdapter(list,this@CreateFragment.context!!)
            container.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        var builder = AlertDialog.Builder(this@CreateFragment.activity!!)

        builder.setTitle("Ingredients")

        builder.setView(list_view_layout_view)

        builder.setPositiveButton("Done",null)

        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(adapter.isChecked.isEmpty()){
                    p0!!.dismiss()
                    return
                }
                for(i in 0..adapter.count - 1){
                    adapter.isChecked.set(i,false)
                    adapter.qty.set(i,"")
                }
                p0!!.dismiss()
            }
        })

        builder.setView(list_view_layout_view)

        var alert = builder.create()

        add_ingredient!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                alert.setOnShowListener(object : DialogInterface.OnShowListener{
                    override fun onShow(p0: DialogInterface?) {
                        var btn = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                        Log.e("size qty",adapter.qty.size.toString())
                        Log.e("size checked",adapter.isChecked.size.toString())
                        btn.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(p0: View?) {
                                var t = false
                                for(i in 0..adapter.count-1){
                                    Log.e("i",i.toString())
//                                        var curr = adapter.items.get(i).findViewById<EditText>(R.id.create_ingredient_qty)
//                                        var container =  adapter.items.get(i).findViewById<TextInputLayout>(R.id.create_ingredient_input_layout)
                                    var qty = adapter.qty.get(i)
                                    if(adapter.isChecked.get(i) && qty.isEmpty()){
                                        t = true
                                    }
                                }
                                //gaskan insert
                                if(t){
                                    Toast.makeText(this@CreateFragment.context,"You must filled all ingredients quantity",Toast.LENGTH_LONG).show()
                                    return
                                }
                                alert.dismiss()
                            }

                        })
                    }
                })
                alert.show()
                alert.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.or(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM))

            }
        })

        create!!.setOnClickListener{
            var isError = false
            if(uri == null){
                Toast.makeText(this@CreateFragment.context,"You must insert recipe image",Toast.LENGTH_SHORT).show()
                isError = true
            }else if(name.text.isEmpty()){
                name.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error_red_24dp,0)
                isError = true
            }
            else if(desc.text.isEmpty()){
                desc.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error_red_24dp,0)
                isError = true
            }else if(serving.text!!.isEmpty()){
                serving.setError("Field cannot be empty")
                isError = true
            }else if(prep.text!!.isEmpty()){
                prep.setError("Field cannot be empty")
                isError = true
            }else if(cook.text!!.isEmpty()){
                cook.setError("Field cannot be empty")
                isError = true
            }else if(step.text.isEmpty()){
                step.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error_red_24dp,0)
                isError = true
            }else{
                var none = true
                for(i in 0..adapter.count - 1){
                    if(adapter.isChecked.get(i) == true){
                        none = false
                    }
                }
                if(none){
                    add_ingredient!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_error_red_24dp,0)
                    isError = true
                }
            }

            if(!isError){
                var ingredient : HashMap<String,Ingredient> = HashMap<String,Ingredient>()
                for(i in 0..adapter.count - 1){
                    if(adapter.isChecked.get(i) == true){
                        ingredient.put(i.toString(),adapter.list.get(i))
                        adapter.isChecked.set(i,false)
                        adapter.qty.set(i,"")
                    }
                }
                var type = MimeTypeMap.getSingleton()
                var ext = type.getExtensionFromMimeType(context!!.contentResolver
                    .getType(uri!!))
                var i = Recipe()
                i.RecipeID = db.document().id
                i.RecipeImage = "recipes/${i.RecipeID}.${ext}"
                i.RecipeName = name.text.toString()
                i.RecipeDesc = desc.text.toString()
                i.Servings = serving.text.toString()
                i.PrepTime = "${prep.text} min"
                i.CookTime = "${cook.text} min"
                i.CookingSteps = "${step.text}"
                i.Ingredients = ingredient
                i.UserId = FirebaseAuth.getInstance().currentUser!!.uid
                db.document(i.RecipeID.toString())
                    .set(i).addOnSuccessListener {
                    Toast.makeText(this@CreateFragment.context,"Successfully add recipe",Toast.LENGTH_LONG).show()
                    name.setText("")
                    desc.setText("")
                    serving.setText("")
                    prep.setText("")
                    cook.setText("")
                    step.setText("")
                    image!!.setImageIcon(null)
                    FirebaseStorage.getInstance()
                        .reference
                        .child("recipes/${i.RecipeID}.${ext}")
                        .putFile(uri!!)
                        .addOnSuccessListener {
                            Log.e("Success","Successfully upload image")
                            Toast.makeText(this@CreateFragment.context,"Successfully uploaded image",Toast.LENGTH_SHORT).show()
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
