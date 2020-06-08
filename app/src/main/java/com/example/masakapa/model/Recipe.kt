package com.example.masakapa.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Recipe (
    var UserId : String? = "",
    var RecipeID : String? = "",
    var RecipeImage : String? = null,
    var RecipeName : String? = "",
    var RecipeDesc : String? = "",
    var Servings : String? = "",
    var PrepTime : String? = "",
    var CookTime : String? = "",
    var CookingSteps : String? = "",
    var Ingredients : Map<String,Ingredient>? = null,
    var TotalLikes : Int? = 0
){

}