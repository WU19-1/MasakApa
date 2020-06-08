package com.example.masakapa.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import com.example.masakapa.model.Comment
import com.example.masakapa.model.Recipe
import com.example.masakapa.recipe.RecipeDetail
import com.example.masakapa.viewholder.CommentViewHolder
import com.example.masakapa.viewholder.ExtendsViewHolder
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.util.*

class ExtendableAdapter (val list : Vector<Recipe>, var ctx : Activity, var manager : WindowManager) :
    RecyclerView.Adapter<ExtendsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtendsViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.history_row,parent,false)
        var holder = ExtendsViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ExtendsViewHolder, position: Int) {
        var curr = list.get(position)
        holder.food_desc.text = curr.RecipeDesc
        holder.food_name.text = curr.RecipeName
        holder.likes.text = curr.TotalLikes.toString()
        FirebaseStorage.getInstance().reference.child(curr!!.RecipeImage!!).getBytes(8096*8096)
            .addOnSuccessListener {
                var bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                holder.image.setImageBitmap(bitmap)
            }

        holder.container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i = Intent(ctx,RecipeDetail::class.java)
                i.putExtra("rid",curr.RecipeID)
                ctx.startActivity(i)
            }
        })
    }

}