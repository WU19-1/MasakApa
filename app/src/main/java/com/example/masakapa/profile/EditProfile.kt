package com.example.masakapa.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.masakapa.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfile : AppCompatActivity() {

    var uri: Uri? = null

    fun pick_image(){
        var i = Intent()
        i.type = "image/*"
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i,1001)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 1001){
            edit_profile_pp!!.setImageURI(null)
            uri = data?.data
            edit_profile_pp!!.setImageURI(uri)
        }else{
            Log.e("error",resultCode.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        var u : com.example.masakapa.model.User? = null

        FirebaseFirestore.getInstance().collection("users").document(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).get().addOnSuccessListener{
            u = it.toObject(com.example.masakapa.model.User::class.java)
            edit_profile_name.setText(u!!.FullName)
            edit_profile_status.setText(u!!.Status)
            if(!u!!.Image!!.isEmpty()){
                FirebaseStorage.getInstance().reference.child(u!!.Image!!).getBytes(8096*8096).addOnSuccessListener{
                    if(it.isNotEmpty()){
                        edit_profile_pp.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
                    }
                }
            }
        }

        edit_profile_change_pp.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this@EditProfile,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1001)
            }else{
                pick_image()
            }
        }

        edit_profile_change_pw.setOnClickListener{

        }

        edit_profile_update_profile.setOnClickListener {
            var isChanged = false
            if(!edit_profile_name.text.equals(u!!.FullName)){
                isChanged = true
                u!!.FullName = edit_profile_name.text.toString()
            }
            if(!edit_profile_status.text.equals(u!!.Status)){
                isChanged = true
                u!!.Status = edit_profile_status.text.toString()
            }
            if(isChanged){
                FirebaseFirestore.getInstance().collection("users").document(
                    FirebaseAuth.getInstance().currentUser!!.uid
                ).set(u!!).addOnSuccessListener {
                    if(uri != null){
                        var type = MimeTypeMap.getSingleton()
                        var ext = type.getExtensionFromMimeType(this@EditProfile.contentResolver
                            .getType(uri!!))
                        FirebaseStorage.getInstance()
                            .reference
                            .child("users/${FirebaseAuth.getInstance().currentUser!!.uid}.${ext}")
                            .putFile(uri!!)
                        u!!.Image = "recipes/${FirebaseAuth.getInstance().currentUser!!.uid}.${ext}"
                    }
                    Toast.makeText(this@EditProfile,"Successfully update user",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@EditProfile,"There is no changes!",Toast.LENGTH_LONG).show()
            }
        }


    }
}
