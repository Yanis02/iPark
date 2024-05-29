package com.example.ipark_project.presentation

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ipark_project.buisiness.viewmodels.MyBookingViewModel
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
fun ExpandableCard(
    title: String,
    fromDate: String,
    toDate: String,
    qrcode: String,
    titleFontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    shape: Shape = RoundedCornerShape(15.dp),
    padding: Dp = 12.dp
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .width(350.dp)
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(15.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(2f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Drop-Down Arrow",
                        tint = Color(0xFFD52D2D),
                    )
                }
            }

            if (expandedState) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = "From:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text(text = fromDate, fontWeight = FontWeight.Light, fontSize = 18.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = "To:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text(text = toDate, fontWeight = FontWeight.Light, fontSize = 18.sp)
                }
                if (qrcode.isNotEmpty()) {
                    val imageBytes = Base64.getDecoder().decode(qrcode)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun myBookings(myBookingViewModel: MyBookingViewModel) {
    val context = LocalContext.current
    // Observe error state and show toast if needed
    val error by remember { myBookingViewModel.error }
    val errorMessage by remember { myBookingViewModel.errorMessage }

    LaunchedEffect(error) {
        if (error) {
            showToast(errorMessage, context)
            myBookingViewModel.errorHandled()  // Reset the error state after handling
        }
    }

    LaunchedEffect(Unit) {
        myBookingViewModel.getAllReservations()
    }

    if (myBookingViewModel.error.value) {
        showToast(myBookingViewModel.errorMessage.value, context)
        myBookingViewModel.error.value = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My",
                    fontSize = 25.sp,
                    color = Color(0xFFD52D2D),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = "Bookings",
                    fontSize = 25.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        myBookingViewModel.reservations.value?.let { reservations ->
            itemsIndexed(reservations) { index, reservation ->
                ExpandableCard(
                    title = reservation.parking,
                    fromDate = reservation.entry_date,
                    toDate = reservation.exit_date,
                    qrcode = reservation.qrcode
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
@Preview
fun ExpandableCardPreview() {
    ExpandableCard(
        title = "Parking du nord",
        fromDate = "18/05/2024 20:04",
        toDate = "18/05/2024 20:04",
        qrcode = ""

    )
}