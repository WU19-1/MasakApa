package com.example.masakapa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import com.example.masakapa.model.Comment
import com.example.masakapa.model.User
import com.example.masakapa.viewholder.CommentViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CommentAdapter (val list : Vector<Comment>, var ctx : Context, var manager : WindowManager) :
    RecyclerView.Adapter<CommentViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.comment_row,parent,false)
        var holder = CommentViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var curr = list.get(position)
        holder.comment_content.text = curr.Comment
        holder.comment_created_at.text = curr.Created_At
        FirebaseFirestore.getInstance().collection("users")
            .document(curr.UserID!!).get().addOnSuccessListener {
                var u = it.toObject(User::class.java)
                holder.comment_creator.text = u!!.FullName
                //ambil foto dari firebase storage
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}