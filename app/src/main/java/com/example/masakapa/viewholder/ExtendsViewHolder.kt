package com.example.masakapa.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.history_row.view.*

class ExtendsViewHolder : RecyclerView.ViewHolder {
    lateinit var image : ImageView
    lateinit var food_name : TextView
    lateinit var food_desc : TextView
    lateinit var likes : TextView
    lateinit var container : LinearLayout

    constructor(view : View) : super(view){
        image = view.findViewById(R.id.history_food_image)
        food_name = view.findViewById(R.id.history_food_name)
        food_desc = view.findViewById(R.id.history_food_description)
        likes = view.findViewById(R.id.history_total_likes)
        container = view.findViewById(R.id.history_container_view)
    }

}