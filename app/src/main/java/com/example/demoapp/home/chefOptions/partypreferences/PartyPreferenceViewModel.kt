package com.example.demoapp.home.chefOptions.partypreferences

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.home.data.PartyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartyPreferenceViewModel @Inject constructor() : ViewModel() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val bookingRef: DatabaseReference = database.getReference("PartyBookings")


    var amountPayable by mutableStateOf("")


    fun updateAmountPayable(amount: String) {
        amountPayable = amount
    }

    var partyDate by mutableStateOf("")


    fun updatePartyDate(date: String) {
        partyDate = date
    }

    var partyTime by mutableStateOf("")


    fun updatePartyTime(time: String) {
        partyTime = time
    }

    var adults by mutableStateOf(0)
    fun updateAdults(adults: Int) {
        this.adults = adults
    }

    var children by mutableStateOf(0)
    fun updateChildren(children: Int) {
        this.children = children
    }

    var starters by mutableStateOf(0)
    fun updateStarters(starters: Int) {
        this.starters = starters
    }

    var mainCourse by mutableStateOf(0)
    fun updateMainCourse(mainCourse: Int) {
        this.mainCourse = mainCourse
    }

    var dessert by mutableStateOf(0)
    fun updateDessert(dessert: Int) {
        this.dessert = dessert
    }

    var sides by mutableStateOf(0)
    fun updateSides(sides: Int) {
        this.sides = sides
    }

    var bartender by mutableStateOf(0)
    fun updateBartender(bartender: Int) {
        this.bartender = bartender
    }

    var waiter by mutableStateOf(0)
    fun updateWaiter(waiter: Int) {
        this.waiter = waiter
    }


    fun saveBookingData(bookingData: PartyData) {
        viewModelScope.launch {
            val bookingId = bookingRef.push().key // Generate a unique ID
            if (bookingId != null) {
                bookingRef.child(bookingId).setValue(bookingData)
                    .addOnSuccessListener {
                        Log.d("BookingViewModel", "Booking data saved successfully: $bookingId")
                    }
                    .addOnFailureListener { e ->
                        Log.e("BookingViewModel", "Error saving booking data", e)
                    }
            } else {
                Log.e("BookingViewModel", "Error generating booking ID")
            }
        }
    }


    fun getLastBooking(
        onSuccess: (PartyData?) -> Unit,
        onError: (String) -> Unit
    ) {
        bookingRef.orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        // Get the last child (which is the last entry)
                        val lastChild = snapshot.children.last()
                        val lastBooking = lastChild.getValue(PartyData::class.java)
                        onSuccess(lastBooking)
                    } else {
                        // No bookings found
                        onSuccess(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }
}