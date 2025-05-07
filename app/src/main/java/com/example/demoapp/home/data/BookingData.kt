package com.example.demoapp.home.data

data class BookingData(
    var partyDate: String = "",
    var partyStartTime: String = "",
    var dinersCount: Int = 0,
    var amountPayable: String = "",
    var userName : String = "",
    var userId : String = "",
    var bookingType:String = "Instant Chef"
)