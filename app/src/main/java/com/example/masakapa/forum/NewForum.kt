package com.example.masakapa.forum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.masakapa.R
import com.example.masakapa.model.Forum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_forum.*
import java.util.*
import kotlin.collections.HashMap

class NewForum : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_forum)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        var db = FirebaseFirestore.getInstance().collection("forums")
        var calendar = Calendar.getInstance()

        new_forum_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var isError = false
                new_forum_title.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
                new_forum_desc.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)

                if (new_forum_title.text.toString().isEmpty()){
                    new_forum_title.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error_red_24dp,0)
                    isError = true
                    Toast.makeText(this@NewForum,"Forum title cannot be empty",Toast.LENGTH_LONG).show()
                }else if(new_forum_desc.text.toString().isEmpty()){
                    new_forum_desc.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error_red_24dp,0)
                    isError = true
                    Toast.makeText(this@NewForum,"Forum description cannot be empty",Toast.LENGTH_LONG).show()
                }

                if(!isError){
                    var f = Forum(db.id,FirebaseAuth.getInstance().currentUser!!.uid,new_forum_desc.text.toString(),
                        new_forum_title.text.toString(),
                        "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH)}-${calendar.get(
                            Calendar.YEAR)},${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}")
                    db.document(f.ForumID.toString()).set(hashMapOf(
                        "UserID" to f.UserID,
                        "Created_at" to f.Created_at,
                        "ForumID" to f.ForumID,
                        "Title" to f.Title,
                        "Description" to f.Description
                    )as HashMap<String, Any>).addOnSuccessListener{
                        Toast.makeText(this@NewForum,"Successfully added thread",Toast.LENGTH_LONG).show()
                        this@NewForum.finish()
                    }
                }
            }
        })
    }
}
