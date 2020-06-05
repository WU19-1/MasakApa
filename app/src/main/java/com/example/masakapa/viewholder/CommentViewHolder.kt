package com.example.masakapa.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import de.hdodenhof.circleimageview.CircleImageView

class CommentViewHolder : RecyclerView.ViewHolder{

    lateinit var comment_pp : CircleImageView
    lateinit var comment_creator : TextView
    lateinit var comment_created_at : TextView
    lateinit var comment_content : TextView

    constructor(view : View) : super(view){
        comment_pp = view.findViewById(R.id.comment_pp)
        comment_creator = view.findViewById(R.id.comment_creator)
        comment_created_at = view.findViewById(R.id.comment_created_at)
        comment_content = view.findViewById(R.id.comment_content)
    }
}