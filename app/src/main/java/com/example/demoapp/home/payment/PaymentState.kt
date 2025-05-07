package com.example.demoapp.home.payment

sealed class PaymentState {
    data object Idle: PaymentState()
    data class Success(val paymentId: String): PaymentState()
    data class Error(val message: String): PaymentState()
   // data class Loading(val isLoading: Boolean): PaymentState()

}