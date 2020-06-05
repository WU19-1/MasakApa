package com.example.masakapa.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Forum(
    var ForumID : String? = "",
    var UserID : String? = "",
    var Description : String? = "",
    var Title : String? = "",
    var Created_at : String? = ""
){

}