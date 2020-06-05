package com.example.masakapa.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.masakapa.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class IngredientViewHolder {
    lateinit var input_layout : TextInputLayout
    lateinit var input_edit : TextInputEditText
    lateinit var ingredient_name : TextView
    lateinit var check : CheckBox

    constructor(view : View){
        input_layout = view.findViewById(R.id.create_ingredient_input_layout)
        input_edit = view.findViewById(R.id.create_ingredient_qty)
        ingredient_name = view.findViewById(R.id.create_ingredient_name)
        check = view.findViewById(R.id.create_ingredient_checkbox)
    }
}