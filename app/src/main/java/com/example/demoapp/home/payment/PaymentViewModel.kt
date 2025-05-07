package com.example.demoapp.home.payment

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.razorpay.Checkout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class PaymentViewModel : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState


    fun startPayment(activity: Activity, amount: Int, description: String = "test payment") {
        try {
            val options = JSONObject().apply {
                put("name", "ChefSphere")
                put("description", description)
                put("currency", "INR")
                put("amount", (amount * 100).toLong())
                put("theme", "#3399cc")
                put("method",JSONObject().apply {
                    put("upi",true)
                    put("qr",true)
                })
                put("upi",JSONObject().apply {
                    put("flow","intent")
                })
                put("readonly",JSONObject().apply {
                    put("contact",true)
                    put("email",true)
                    put("method",true)
                })

            }
            val checkout = Checkout()
            checkout.setKeyID(RazorpayConfig.KEY_ID)
            checkout.open(activity, options)

        }catch (e : Exception){
            _paymentState.value = PaymentState.Error(e.message.toString())
        }
    }
    fun handlePaymentSuccess(paymentId: String){
        _paymentState.value = PaymentState.Success(paymentId)
    }
    fun handlePaymentError(code:Int,errorMessage: String){
        _paymentState.value = PaymentState.Error(errorMessage)
    }
}