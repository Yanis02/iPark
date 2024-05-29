package com.example.ipark_project.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ipark_project.buisiness.URL
import com.example.ipark_project.buisiness.entities.Parking
import com.example.ipark_project.buisiness.viewmodels.CreateReservationViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun searchParkingsForm(parking: Parking?, navController: NavController, createReservationViewModel: CreateReservationViewModel) {
    val applicationContext = LocalContext.current
    var pickedEnteryDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedEnteryTime by remember { mutableStateOf(LocalTime.NOON) }
    var pickedExitDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedExitTime by remember { mutableStateOf(LocalTime.NOON) }

    val formattedEnteryDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(pickedEnteryDate)
        }
    }
    val formattedEnteryTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("HH:mm:ss").format(pickedEnteryTime)
        }
    }
    val formattedExitDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(pickedExitDate)
        }
    }
    val formattedExitTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("HH:mm:ss").format(pickedExitTime)
        }
    }

    val dateEntryDialogState = rememberMaterialDialogState()
    val timeEntryDialogState = rememberMaterialDialogState()
    val dateExitDialogState = rememberMaterialDialogState()
    val timeExitDialogState = rememberMaterialDialogState()

    val context = LocalContext.current

    if (createReservationViewModel.error.value){
        showToast(createReservationViewModel.errorMessage.value, context)
        createReservationViewModel.error.value = false
    }

    if (createReservationViewModel.success.value){
        showToast("Reservation created", context)
        createReservationViewModel.success.value = false
        navController.navigate(Router.ReservationsScreen.route)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Content
        }

        Text(text = "Entery information", fontSize = 20.sp, color = Color(0xFFD52D2D))

        MaterialDialog(
            dialogState = dateEntryDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(applicationContext, "Clicked ok", Toast.LENGTH_LONG).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date"
            ) { pickedEnteryDate = it }
        }

        MaterialDialog(
            dialogState = timeEntryDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(applicationContext, "Clicked ok", Toast.LENGTH_LONG).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "Pick a time"
            ) { pickedEnteryTime = it }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp, 60.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .border(
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { dateEntryDialogState.show() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Pick entery date", fontSize = 15.sp, color = Color.Black)
                }
                Text(text = formattedEnteryDate, modifier = Modifier.width(100.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp, 60.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .border(
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { timeEntryDialogState.show() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Pick entery time", fontSize = 15.sp, color = Color.Black)
                }
                Text(text = formattedEnteryTime, modifier = Modifier.width(100.dp))
            }
        }

        Text(text = "Exit information", fontSize = 20.sp, color = Color(0xFFD52D2D))

        MaterialDialog(
            dialogState = dateExitDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(applicationContext, "Clicked ok", Toast.LENGTH_LONG).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date"
            ) { pickedExitDate = it }
        }

        MaterialDialog(
            dialogState = timeExitDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(applicationContext, "Clicked ok", Toast.LENGTH_LONG).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "Pick a time"
            ) { pickedExitTime = it }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp, 60.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .border(
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { dateExitDialogState.show() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Pick exit date", fontSize = 15.sp, color = Color.Black)
                }
                Text(text = formattedExitDate, modifier = Modifier.width(100.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp, 60.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .border(
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { timeExitDialogState.show() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Pick exit time", fontSize = 15.sp, color = Color.Black)
                }
                Text(text = formattedExitTime, modifier = Modifier.width(100.dp))
            }
        }

        Row(
            modifier = Modifier
                .size(150.dp, 60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFD52D2D))
                .clickable {

                    if (parking != null) {
                        createReservationViewModel.CreateReservation(
                            parking = parking.id,
                            entry_date = formattedEnteryDate,
                            entry_time = formattedEnteryTime,
                            exit_date = formattedExitDate,
                            exit_time = formattedExitTime
                        )
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Book Parking",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun parkingDetails(
   parking: Parking?,
   navController: NavController,
   createReservationViewModel: CreateReservationViewModel
) {
    var sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { parking?.images?.size ?: 0 }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (parking != null) {
                    HorizontalPager(
                        state = pagerState,
                        key = { parking.images[it] }
                    ) { index ->
                        println(URL+parking.images[index].imgFile)
                        AsyncImage(
                            model = URL+parking.images[index].imgFile,
                            contentScale = ContentScale.Crop,
                            contentDescription = "parking photos",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 7.dp, end = 15.dp, bottom = 7.dp)
            ) {
                if (parking != null) {
                    Text(
                        text = parking.name,
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "",
                    tint = Color(0xFFD52D2D),
                    modifier = Modifier.clickable {
                        isFavorite = !isFavorite
                    }
                )
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 7.dp, end = 15.dp, bottom = 7.dp)
            ) {
                Box(
                    modifier = Modifier.width(200.dp)
                ) {
                    if (parking != null) {
                        Text(
                            text = parking.address,
                            color = Color(0xFF999999),
                            fontSize = 18.sp,
                            maxLines = 4,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (parking != null) {
                    Text(
                        text = "${parking.price}.price DZD",
                        color = Color(0xFFD52D2D),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 7.dp, end = 15.dp, bottom = 7.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Description",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                if (parking != null) {
                    Text(
                        text = parking.description,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    ,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(
                    modifier = Modifier
                        .size(150.dp, 60.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFD52D2D))
                        .clickable { showBottomSheet = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Book Parking",
                        color = Color.White,
                        fontSize = 18.sp,

                    )

                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            searchParkingsForm(
                parking = parking,
                navController = navController,
                createReservationViewModel = createReservationViewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
@Preview
fun detailsPreview(){
    val images= listOf<String>(
        "https://picsum.photos/200",
        "https://picsum.photos/300",
        "https://picsum.photos/400"
    )
    //parkingDetails(name = "Parking du nord", address ="Alger , Alger Bve des martyres" , description ="In this example, the Text composable is wrapped inside a Box, which has a width set to 50% of its parent's width using the fillMaxWidth() and weight(0.5f) modifiers. Additionally, the Text composable itself has the fillMaxWidth() modifier applied, ensuring that it fills the available width of its parent (Box)." , price ="250" , imageUrls =images,liked=false )
}
