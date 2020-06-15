package com.example.masakapa.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var UID : String? = "",
    var FullName : String? = "",
    var Subscription : String? = "",
    var Email : String? = "",
    var DOB : String? = "",
    var CardNumber : String? = "",
    var Status : String? = "",
    var Image : String? = ""
){


}



