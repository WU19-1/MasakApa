package com.example.masakapa.recipe

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.masakapa.R
import com.example.masakapa.model.Favorite
import com.example.masakapa.model.History
import com.example.masakapa.model.Like
import com.example.masakapa.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class RecipeDetail : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        var r : Recipe? = null

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        var id = intent.extras!!.getString("rid")

        Log.e("id",id)

        var h : History? = null
        var calendar = Calendar.getInstance()

        FirebaseFirestore.getInstance().collection("history").document(id!!).get().addOnSuccessListener{
            if(it.exists()){
                h = it.toObject(History::class.java)
                var time = h!!.history!!.getOrDefault(id!!,"")
                h!!.history!!.set(id!!,"${calendar.get(Calendar.DAY_OF_MONTH)}-" +
                        "${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.YEAR)}," +
                        "${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}")
                FirebaseFirestore.getInstance().collection("history").document(id!!).set(
                    h!!
                )
            }else{
                h = History()
                h!!.history = hashMapOf(
                    id!! to "${calendar.get(Calendar.DAY_OF_MONTH)}-" +
                            "${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.YEAR)}," +
                            "${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}"
                )
                FirebaseFirestore.getInstance().collection("history").document(id!!).set(
                    h!!
                )
            }
        }

        FirebaseFirestore.getInstance().collection("recipes").document(id!!).get().addOnSuccessListener{
            r = it.toObject(Recipe::class.java)
            detail_recipe_name.text = r!!.RecipeName
            detail_recipe_desc.text = r!!.RecipeDesc
            detail_recipe_cook.text = r!!.CookTime + "(s) cook time"
            detail_recipe_prep.text = r!!.PrepTime + "(s) prep time"
            detail_recipe_serving.text = r!!.Servings + " Serving(s)"
            detail_recipe_total.text = (Integer.parseInt(r!!.CookTime!!.split(" ")[0].toString())
                    + Integer.parseInt(r!!.PrepTime!!.split(" ")[0].toString())).toString() + " min(s) total"
            FirebaseStorage.getInstance().reference.child(r!!.RecipeImage!!).getBytes(8096*8096)
                .addOnSuccessListener {
                var bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                detail_recipe_image.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
            step_content.text = r!!.CookingSteps
            var str = ""
            for(i in r!!.Ingredients!!.toList()){
                str += "${i.second.Name} ${i.second.Qty} ${i.second.Unit}\n"
            }
            ingredients_content.text = str
        }

        var ingre_view = false
        var step_view = false

        detail_ingredient.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ingre_view = !ingre_view
                if(ingre_view){
                    ingredients_content.visibility = View.GONE
                }else{
                    ingredients_content.visibility = View.VISIBLE
                    step_content.visibility = View.GONE
                }

            }
        })

        detail_steps.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                step_view = !step_view
                if(step_view){
                    step_content.visibility = View.GONE
                }else{
                    ingredients_content.visibility = View.GONE
                    step_content.visibility = View.VISIBLE
                }
            }
        })

        var db = FirebaseFirestore.getInstance()
        var mAuth = FirebaseAuth.getInstance()

        db.collection("likes").document(mAuth.currentUser!!.uid).get().addOnSuccessListener{
            var l = it.toObject(Like::class.java)
            if(it.exists()){
                var liked = l!!.liked!!.getOrDefault(id,false)
                if(liked){
                    detail_like_btn.text = "Unlike"
                }else{
                    detail_like_btn.text = "Like"
                }
            }
        }

        detail_like_btn.setOnClickListener(object : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onClick(p0: View?) {
                var like : Like? = null
                db.collection("likes").document(mAuth.currentUser!!.uid).get().addOnSuccessListener{
                    like = it.toObject(Like::class.java)
                    if(it.exists()){
                        var bool = false
                        if(detail_like_btn.text.equals("Unlike")){
                            bool = false
                        }else{
                            bool = true
                        }
                        like!!.liked!!.set(id,bool)
                        db.collection("likes").document(mAuth.currentUser!!.uid)
                            .set(
                                like!!
                            ).addOnSuccessListener{
                                if(bool!!){
                                    detail_like_btn.text = "Unlike"
                                    r!!.TotalLikes = r!!.TotalLikes!! + 1
                                    FirebaseFirestore.getInstance().collection("recipes").document(id!!).set(r!!).addOnSuccessListener {
                                        Toast.makeText(this@RecipeDetail,"Liked!",Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    detail_like_btn.text = "Like"
                                    r!!.TotalLikes = r!!.TotalLikes!! - 1
                                    FirebaseFirestore.getInstance().collection("recipes").document(id!!).set(r!!)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@RecipeDetail,"Liked!",Toast.LENGTH_LONG).show()
                                        }
                                }
                            }
                    }else{
                        db.collection("likes").document(mAuth.currentUser!!.uid)
                            .set(
                                Like(hashMapOf(
                                    id.toString() to true
                                ))
                            ).addOnSuccessListener{
                                r!!.TotalLikes = r!!.TotalLikes!! + 1
                                FirebaseFirestore.getInstance().collection("recipes").document(id!!).set(r!!).addOnSuccessListener {
                                    Toast.makeText(this@RecipeDetail,"Liked!",Toast.LENGTH_LONG).show()
                                }
                            }
                        detail_like_btn.text = "Unlike"
                    }
                }

            }
        })

        detail_fav_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                db.collection("favorite").document(mAuth.currentUser!!.uid).get().addOnSuccessListener{
                    var lists = ArrayList<String>()
                    var curr = it.toObject(Favorite::class.java)
                    var ex = false
                    if(it.exists()){
                        lists = curr!!.fav!!
                        for(i in lists!!){
                            if(i.equals(id)){
                                Toast.makeText(this@RecipeDetail,"This recipe is already in your favorite list",Toast.LENGTH_LONG).show()
                                ex = true
                                break
                            }
                        }
                        if(!ex){
                            curr.fav!!.add(id)
                            db.collection("favorite").document(mAuth.currentUser!!.uid).set(
                                curr
                            )
                        }
                    }else{
                        var new = Favorite()
                        new.fav = ArrayList<String>()
                        new.fav!!.add(id)
                        db.collection("favorite").document(mAuth.currentUser!!.uid).set(
                            new
                        ).addOnSuccessListener {
                            Toast.makeText(this@RecipeDetail,"Added to your favorite list!",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })



    }
}
