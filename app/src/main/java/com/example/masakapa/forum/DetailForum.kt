package com.example.masakapa.forum

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masakapa.R
import com.example.masakapa.adapter.CommentAdapter
import com.example.masakapa.model.Comment
import com.example.masakapa.model.Forum
import com.example.masakapa.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_detail_forum.*
import java.lang.Exception
import java.util.*

class DetailForum : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_forum)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        var db = FirebaseFirestore.getInstance()
        var comment = db.collection("comments").document()

        var fid = intent.extras!!.getString("forum_id","").trim()

        var list : Vector<Comment> = Vector()
        var preview : Vector<Comment> = Vector()
        preview.clear()
        list.clear()

        var first_time = true

        comment_container.layoutManager = LinearLayoutManager(this@DetailForum)
        comment_container.adapter = CommentAdapter(preview,this@DetailForum, windowManager)

        var view = layoutInflater.inflate(R.layout.alert_dialog_insert_comment,null)

        var edit_text = view.findViewById<EditText>(R.id.dialog_comment)
        var error_text = view.findViewById<TextView>(R.id.dialog_error)

        edit_text.maxLines = 1
        edit_text.setSingleLine()
        edit_text.imeOptions = EditorInfo.IME_ACTION_DONE

        var calendar = Calendar.getInstance()

        var alert = AlertDialog.Builder(this@DetailForum)

        var id_error = R.drawable.ic_error_red_24dp

        if(fid.isEmpty() == false){
            alert.setTitle("Insert comment")
            alert.setCancelable(true)
            alert.setView(view)
            alert.setPositiveButton("OK",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    edit_text.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
                    if(edit_text.text.isEmpty()){
                        error_text.text = "Comment cannot be empty!"
                        edit_text.setCompoundDrawablesWithIntrinsicBounds(0,0,id_error,0)

                    }
                    else{
                        var c = Comment()
                        c.UserID = FirebaseAuth.getInstance().currentUser!!.uid
                        c.ForumID = fid
                        c.Comment = edit_text.text.toString()
                        c.Created_At =
                            "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.YEAR)},${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}"
                        db.collection("comments").document(comment.id).set(hashMapOf(
                                "UserID" to c.UserID,
                                "CommentID" to comment.id,
                                "Comment" to c.Comment,
                                "Created_At" to c.Created_At,
                                "ForumID" to c.ForumID
                        ) as Map<String, Any>)
                    }
                }
            })
            alert.setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0!!.cancel()
                }
            })
            db.collection("forums").document(fid)
                .get().addOnSuccessListener {
                    var forum = it.toObject(Forum::class.java)
                    detail_forum_created_at.text = forum!!.Created_at
                    detail_forum_title.text = forum!!.Title
                    detail_forum_desc.text = forum!!.Description
                    db.collection("users")
                        .document(forum!!.UserID!!)
                        .get().addOnSuccessListener {
                            var user = it.toObject(User::class.java)
                            detail_forum_creator.text = user!!.FullName
                            //change profile pic
                        }
                    //load total of likes and comments
                    db.collection("comments")
                        .whereEqualTo("ForumID",fid)
                        .get().addOnSuccessListener {
                            for(q in it){
                                var com = q.toObject(Comment::class.java)
                                Log.e("data",com.Comment)
                                list.add(com)
                            }
                            try{
                                if(list.size <= 3){
                                    for(i in 0..list.size-1){
                                        Log.e("index < 3","${i}")
                                        preview.add(list.get(i))
                                    }
                                }else if(list.size > 3){
                                    for(i in 0..2){
                                        Log.e("index > 3","${i}")
                                        preview.add(list.get(i))
                                    }
                                }
                                comment_container.adapter = CommentAdapter(preview,this@DetailForum, windowManager)
                                Log.e("size of preview","${preview.size}")
                                Log.e("size of list","${list.size}")
                            }catch (e: Exception){
                                Log.e("eror",e.message)
                            }
                        }
                }
            db.collection("comments").addSnapshotListener(object :
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                    if(first_time){
                        first_time = false
                        return
                    }
                    for (q in p0!!.getDocumentChanges()){
                        when (q.type){
                            DocumentChange.Type.ADDED -> {
                                if(preview.size < 3){
                                    Log.e("masuk","masuk ke alert")
                                    preview.add(q.document.toObject(Comment::class.java))
                                    list.add(q.document.toObject(Comment::class.java))
                                }else{
                                    list.add(q.document.toObject(Comment::class.java))
                                }
                                comment_container.adapter!!.notifyDataSetChanged()
                            }
                            DocumentChange.Type.REMOVED -> {

                                if(preview.size < 3){
                                    preview.remove(q.document.toObject(Comment::class.java))
                                    list.remove(q.document.toObject(Comment::class.java))
                                }else{
                                    list.remove(q.document.toObject(Comment::class.java))
                                }
                                comment_container.adapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
        }

        detail_like.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        detail_comment.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var t = alert.create()
                t.show()
                t.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                edit_text.addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if(p0!!.isEmpty()){
                            t.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                        }else{
                            t.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                        }
                    }

                })
            }
        })

        detail_show_more.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                preview.clear()
                comment_container.adapter = CommentAdapter(preview,this@DetailForum, windowManager)
                comment_container.adapter = CommentAdapter(list,this@DetailForum, windowManager)
            }
        })


    }
}
