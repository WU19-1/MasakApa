package com.example.masakapa.model

class User{
    public var UserID : String? = ""
    public var FullName : String? = ""
    public var Subscription : String? = ""
    public var Email : String? = ""
    public var DOB : String? = ""
    public var CardNumber : String? = ""
    public var Status : String? = ""

    constructor(UserID : String, FullName : String , Subscription : String, Email : String, DOB : String, CardNumber : String, Status : String){
        this.UserID = UserID
        this.FullName = FullName
        this.Subscription = Subscription
        this.Email = Email
        this.DOB = DOB
        this.CardNumber = CardNumber
        this.Status = Status
    }

    constructor(){

    }

}