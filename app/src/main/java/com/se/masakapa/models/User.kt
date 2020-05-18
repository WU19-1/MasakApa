package com.se.masakapa.models

class User {
    var userId: String? = null
    var profilePicture: String
    var email: String
    var displayName: String

    constructor(userId: String?, email: String, displayName: String, profilePicture: String) {
        this.userId = userId
        this.profilePicture = profilePicture
        this.email = email
        this.displayName = displayName
    }

    constructor(email: String, displayName: String, profilePicture: String) {
        this.profilePicture = profilePicture
        this.email = email
        this.displayName = displayName
    }

}