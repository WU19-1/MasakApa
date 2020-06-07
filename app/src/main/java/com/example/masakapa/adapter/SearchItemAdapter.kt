package com.example.masakapa.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.masakapa.R
import com.example.masakapa.model.Comment
import com.example.masakapa.model.Ingredient
import com.example.masakapa.viewholder.CommentViewHolder
import com.example.masakapa.viewholder.SearchIngredientViewHolder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SearchItemAdapter (var list : Vector<Ingredient>, var ctx : Context, var manager : WindowManager,
                         var all_items : ArrayList<Ingredient> = ArrayList(list)
) :
    RecyclerView.Adapter<SearchIngredientViewHolder>(){

    var items = Vector<SearchIngredientViewHolder>()

    var isChecked = Vector<Boolean>()

    var map = TreeMap<String,Boolean>()

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: SearchIngredientViewHolder, position: Int) {
        var curr = list.get(position)
        holder.name.text = curr.Name
        if(!map.isEmpty()){
            holder.checkBox.isChecked = map.getOrDefault(curr.Name!!,false)
        }
        holder.checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                map.set(holder.name.text.toString(),p1)
                if(p1){
                    holder.checkBox.isChecked = true
                }else{
                    holder.checkBox.isChecked = false
                    map.set(holder.name.text.toString(),false)
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchIngredientViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.search_items,parent,false)
        var holder = SearchIngredientViewHolder(view)
        isChecked.add(false)
        items.add(holder)
        return holder
    }

    fun filter(list : Vector<Ingredient>){
        this.list = list
        notifyDataSetChanged()
    }

}