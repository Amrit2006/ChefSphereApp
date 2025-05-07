package com.example.demoapp.home.data

data class PartyData(
    var partyDate: String = "",
    var partyStartTime: String = "",
    var amountPayable: String = "",
    var userName : String = "",
    var userId : String = "",
    var bookingType:String = "Chef for party",
    var adults:Int = 0,
    var children:Int = 0,
    var starters:Int = 0,
    var mainCourse:Int = 0,
    var dessert:Int = 0,
    var sides:Int = 0,
    var bartender:Int = 0,
    var waiter:Int = 0

)