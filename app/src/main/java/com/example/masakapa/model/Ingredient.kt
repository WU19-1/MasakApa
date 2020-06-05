package com.example.masakapa.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Ingredient (
    var IngredientID : String? = "",
    var Name : String? = "",
    var Unit : String? = "",
    var Qty : String? = ""
){

}