package com.example.masakapa.recipe

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.masakapa.R
import com.example.masakapa.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_recipe_detail.*

class RecipeDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        var r : Recipe? = null

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        var id = intent.extras!!.getString("rid")

        Log.e("id",id)

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



    }
}
