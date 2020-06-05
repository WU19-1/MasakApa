package com.example.masakapa.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Comment(
    var CommentID : String? = "",
    var UserID : String? = "",
    var ForumID : String? = "",
    var Comment : String? = "",
    var Created_At : String? = ""
){
}