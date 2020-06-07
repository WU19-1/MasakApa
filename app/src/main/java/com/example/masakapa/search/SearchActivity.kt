package com.example.masakapa.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masakapa.R
import com.example.masakapa.adapter.SearchItemAdapter
import com.example.masakapa.model.Ingredient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    var v = Vector<Ingredient>()
    lateinit var adapter: SearchItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        var checked_items = Vector<Ingredient>()

        search_ingredient_list.layoutManager = LinearLayoutManager(this)
        adapter = SearchItemAdapter(v,this@SearchActivity,windowManager)
        search_ingredient_list.adapter = adapter

        FirebaseFirestore.getInstance().collection("ingredients")
            .get().addOnSuccessListener{
                var x = Vector<Ingredient>()
                var i = 0
                for(q in it){
                    var curr = q.toObject(Ingredient::class.java)
                    curr.position = i++
                    x.add(curr)
                    adapter.map.put(curr.Name!!,false)
                }
                v = x
                adapter = SearchItemAdapter(x,this@SearchActivity,windowManager)
                search_ingredient_list.adapter = adapter
                adapter.notifyDataSetChanged()
            }



        search_ingredient_bar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

        })

        search_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var b = Intent()
                var str = ""
                for(i in adapter.map){
                    if(i.value){
                        Log.e("search",i.key)
                        if(str.isEmpty()) str += i.key
                        else str += ",${i.key}"
                    }
                }
                Log.e("result_of",str)
                if(str.isEmpty()) finish()
                b.putExtra("result",str.trim())
                setResult(1,b)
                finish()
            }
        })
    }

    fun filter(text : String){
        var filter = Vector<Ingredient>()
        for (i in v){
            if(i.Name!!.toLowerCase().contains(text.toLowerCase())){
                filter.add(i)
            }
        }
        adapter.filter(filter)
    }
}
