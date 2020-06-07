package com.example.masakapa.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.masakapa.R
import com.example.masakapa.model.Ingredient
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class IngredientAdapter(val list : Vector<Ingredient>, var ctx : Context) :
    ArrayAdapter<Ingredient>(ctx, R.layout.ingredients_row,list) {

    var items : Vector<View> = Vector()
    var isChecked : Array<Boolean> = Array(list.size, init = {
        false
    })
    var qty : Array<String> = Array(list.size, init = {
        ""
    })

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var curr = list.get(position)

        var inflater = LayoutInflater.from(ctx)
        var holder = inflater.inflate(R.layout.ingredients_row,null,true)
        items.add(holder)

        holder.findViewById<TextView>(R.id.create_ingredient_name).text = curr.Name
        var layout = holder.findViewById<TextInputLayout>(R.id.create_ingredient_input_layout)
        layout.suffixText = curr.Unit
        layout.isErrorEnabled = true
        var edit = holder.findViewById<EditText>(R.id.create_ingredient_qty)
        var checkbox = holder.findViewById<CheckBox>(R.id.create_ingredient_checkbox)
        checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                isChecked.set(position,p1)
                if(p1){
                    edit.isEnabled = true
                }else{
                    edit.isEnabled = false
                }
            }
        })

        edit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                qty.set(position,p0.toString())
            }

        })
        edit.setText(qty.get(position))
        checkbox.isChecked = isChecked.get(position)



        return holder
    }
}