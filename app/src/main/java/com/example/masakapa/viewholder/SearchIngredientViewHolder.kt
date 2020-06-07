package com.example.masakapa.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import kotlinx.android.synthetic.main.search_items.view.*

class SearchIngredientViewHolder : RecyclerView.ViewHolder{

    lateinit var checkBox: CheckBox
    lateinit var name: TextView


    constructor(view : View) : super(view){
        name = view.findViewById(R.id.search_item_ingredient_name)
        checkBox = view.findViewById(R.id.search_item_checkbox)
    }
}