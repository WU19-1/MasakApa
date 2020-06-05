package com.example.masakapa.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.example.masakapa.R
import com.example.masakapa.model.Forum
import com.example.masakapa.model.Ingredient
import com.example.masakapa.viewholder.IngredientViewHolder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class IngredientAdapter(val list : Vector<Ingredient>, var ctx : Context) :
    ArrayAdapter<Ingredient>(ctx, R.layout.ingredients_row,list) {

    var items : Vector<View> = Vector()
    var isChecked : Array<Boolean> = Array(list.size, init = {
        false
    })

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var curr = list.get(position)

        var inflater = LayoutInflater.from(ctx)
        var holder = inflater.inflate(R.layout.ingredients_row,null,true)


        holder.findViewById<TextView>(R.id.create_ingredient_name).text = curr.Name
        holder.findViewById<TextInputLayout>(R.id.create_ingredient_input_layout).suffixText = curr.Unit
        var checkbox = holder.findViewById<CheckBox>(R.id.create_ingredient_checkbox)
        var edit_text = holder.findViewById<TextInputEditText>(R.id.create_ingredient_qty)
        checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                isChecked.set(position,p1)
            }
        })
        checkbox.isChecked = isChecked.get(position)

        items.add(holder)
        return holder
    }
}