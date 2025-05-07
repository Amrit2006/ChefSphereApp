package com.example.demoapp.home.payment


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.R
import com.example.demoapp.home.chefOptions.partypreferences.PartyPreferenceViewModel
import com.example.demoapp.home.data.PartyData
import com.example.demoapp.ui.theme.bookingBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen2(navController: NavController) {
    val viewModel = PaymentViewModel()
    var amount by remember { mutableStateOf("") } // Default amount
    val context = LocalContext.current
    val paymentState by viewModel.paymentState.collectAsState()

    val viewModel2: PartyPreferenceViewModel = hiltViewModel()

    var lastBooking by remember { mutableStateOf<PartyData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(key1 = true) {
        viewModel2.getLastBooking(
            onSuccess = { booking ->
                lastBooking = booking
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }
    amount = lastBooking?.amountPayable.toString() ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bookingBack)
            .padding(top = 30.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Image(
                painter = painterResource(R.drawable.backblackbutton),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                "Order Summary",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        VisitDetails2()

        Information2()
        CancellationPolicy2()




        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                amount.toDoubleOrNull()?.let {
                    viewModel.startPayment(context as Activity, amount.toInt())
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Black),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .padding(20.dp)
                .width(220.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column() {
                Text(
                    "₹$amount", color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    "Total", color = Color.White,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.size(30.dp))
            Text(
                "Book Now",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        when (
            val state: PaymentState = paymentState) {
            is PaymentState.Success -> {
                Text("Razorpay: ${state.paymentId}")
            }

            is PaymentState.Error -> {
                Text("Error: ${state.message}")
            }

            is PaymentState.Idle -> {
                // No-op
            }
        }

    }

}

@Composable
fun VisitDetails2(modifier: Modifier = Modifier) {
    val viewModel2: PartyPreferenceViewModel = hiltViewModel()
    var lastBooking by remember { mutableStateOf<PartyData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel2.getLastBooking(
            onSuccess = { booking ->
                lastBooking = booking
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text("Party Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        if (errorMessage != null) {
            Text("Error: $errorMessage")
        } else if (lastBooking != null) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Date")
                Text(lastBooking!!.partyDate)

            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Time")
                Text(lastBooking!!.partyStartTime + " PM")

            }



            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val totalPerson = lastBooking!!.adults + lastBooking!!.children
                Text("No. of people")
                Text(totalPerson.toString())


            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dishesTotal =
                    lastBooking!!.starters + lastBooking!!.mainCourse + lastBooking!!.dessert + lastBooking!!.sides
                Text("No. of dishes")
                Text(dishesTotal.toString())

            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val addOnsTotal = lastBooking!!.bartender + lastBooking!!.waiter
                Text("Add-Ons")
                Text(addOnsTotal.toString())

            }
        } else {
            Log.e("BookingViewModel", "Error generating booking ID")
        }
    }

}

@Composable
fun Information2(modifier: Modifier = Modifier) {

    val viewModel2: PartyPreferenceViewModel = hiltViewModel()
    var lastBooking by remember { mutableStateOf<PartyData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel2.getLastBooking(
            onSuccess = { booking ->
                lastBooking = booking
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {

        Text("Visit Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        if (errorMessage != null) {
            Text("Error: $errorMessage")
        } else if (lastBooking != null) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.right_arrow_small),
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .padding(end = 10.dp)
                )
                Text("Cook will not responsible for utensil cleaning")

            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val dishesTotal =
                    lastBooking!!.starters + lastBooking!!.mainCourse + lastBooking!!.dessert + lastBooking!!.sides
                Image(
                    painter = painterResource(R.drawable.right_arrow_small),
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .padding(end = 10.dp)
                )
                Text("Cook will prepare upto " +dishesTotal.toString()+ " dish only")

            }
        }else {
            Log.e("BookingViewModel", "Error generating booking ID")
        }

    }
}

@Composable
fun CancellationPolicy2(modifier: Modifier = Modifier) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text("Cancellation Policy", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.right_arrow_small),
                contentDescription = null,
                Modifier
                    .size(24.dp)
                    .padding(end = 10.dp)
            )
            Text("20% cancellation fee if cook is assigned")

        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.right_arrow_small),
                contentDescription = null,
                Modifier
                    .size(24.dp)
                    .padding(end = 10.dp)
            )
            Text("Full refund if cook is not assigned")

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PaymentScreen2Preview() {
    PaymentScreen2(rememberNavController())
}
