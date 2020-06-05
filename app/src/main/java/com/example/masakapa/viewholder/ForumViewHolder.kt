package com.example.masakapa.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import kotlinx.android.synthetic.main.forum_row.view.*

class ForumViewHolder : RecyclerView.ViewHolder{

    lateinit var pp : de.hdodenhof.circleimageview.CircleImageView
    lateinit var title : TextView
    lateinit var desc : TextView
    lateinit var likes : TextView
    lateinit var replies : TextView
    lateinit var creator : TextView
    lateinit var container : LinearLayout

    constructor(view : View) : super(view){
        pp = view.findViewById(R.id.forum_profile_pic)
        title = view.findViewById(R.id.forum_title)
        desc = view.findViewById(R.id.forum_desc)
        likes  = view.findViewById(R.id.forum_total_likes)
        replies = view.findViewById(R.id.forum_total_reply)
        creator = view.findViewById(R.id.forum_creator)
        container = view.findViewById(R.id.forum_container)
    }
}