package com.example.masakapa.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import com.example.masakapa.forum.DetailForum
import com.example.masakapa.model.Forum
import com.example.masakapa.model.User
import com.example.masakapa.viewholder.ForumViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ForumAdapter (val list : Vector<Forum>, var ctx : Context, var manager : WindowManager) :
    RecyclerView.Adapter<ForumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.forum_row,parent,false)
        var holder = ForumViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        var curr = list.get(position)
        if(curr.Title!!.length > 27){
            holder.title.text = curr.Title!!.substring(0,23) + "..."
        }else{
            holder.title.text = curr.Title
        }
        if(curr.Title!!.length > 64){
            holder.desc.text = curr.Description!!.substring(0,61) + "..."
        }else{
            holder.desc.text = curr.Description
        }
        FirebaseFirestore.getInstance()
            .collection("users").document(curr.UserID!!)
            .get().addOnSuccessListener {
                var u = it.toObject(User::class.java)
                holder.creator.text = u!!.FullName
            }
        //untuk komponen lain nanti ditambahin firebase storage
        holder.container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var i = Intent(ctx,DetailForum::class.java)
                i.putExtra("forum_id",curr.ForumID)
                ctx.startActivity(i)
            }
        })
    }

}