package com.ridefinder.presentation.search

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridefinder.presentation.data.constants.Constants
import com.ridefinder.ui.theme.getTextColor
import java.util.Date
import java.util.Locale

/**
 * Created by fasil on 27/02/25.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarRentalSearchScreen(modifier: Modifier = Modifier, viewModel: CarRentalSearchViewModel) {
    var pickupLocation by remember { mutableStateOf("") }
    var pickupDate by remember { mutableStateOf("") }
    var dropOffLocation by remember { mutableStateOf("") }
    var dropOffDate by remember { mutableStateOf("") }
    var showPickupDatePicker by remember { mutableStateOf(false) }
    var showDropOffDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val context = LocalContext.current

    val canSearch by viewModel.canSearch.collectAsState()

    if (showPickupDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (pickupDate.isEmpty()) {
                System.currentTimeMillis()
            } else {
                dateFormatter.parse(pickupDate).time
            }
        )

        DatePickerDialog(
            onDismissRequest = { showPickupDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = dateFormatter.format(Date(millis))
                        pickupDate = date
                        viewModel.updatePickupDate(date)
                    }
                    showPickupDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPickupDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showDropOffDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (dropOffDate.isEmpty()) {
                System.currentTimeMillis()
            } else {
                dateFormatter.parse(dropOffDate).time
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDropOffDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = dateFormatter.format(Date(millis))
                        dropOffDate = date
                    }
                    showDropOffDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDropOffDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Pickup Location",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            value = pickupLocation,
            onValueChange = {
                pickupLocation = it
                viewModel.updatePickupLocation(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter city, airport, or address") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Text(
            text = "Drop-off Location (Optional)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            value = dropOffLocation,
            onValueChange = { dropOffLocation = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter city, airport, or address") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pickup Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = { showPickupDatePicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = if (pickupDate.isEmpty()) Color.Gray else getTextColor()
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = pickupDate.ifEmpty { "Select date" },
                            fontSize = 16.sp,
                            color = if (pickupDate.isEmpty()) Color.Gray else getTextColor()
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Drop-off Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = { showDropOffDatePicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = if (dropOffDate.isEmpty()) Color.Gray else getTextColor()
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = dropOffDate.ifEmpty { "Select date" },
                            fontSize = 16.sp,
                            color = if (dropOffDate.isEmpty()) Color.Gray else getTextColor()
                        )
                    }
                }
            }
        }



        Button(
            onClick = {
                runCatching {
                    val url = Constants.BASE_URL.let { baseUrl ->
                        var result = "$baseUrl$pickupLocation/"
                        if (dropOffLocation.isNotEmpty()) {
                            result += "$dropOffLocation/"
                        }
                        result += "$pickupDate/"
                        if (dropOffDate.isNotEmpty()) {
                            result += dropOffDate
                        }
                        result
                    }
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }.onFailure {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFED6B64)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = canSearch
        ) {
            Text(
                text = "Search on Kayak",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }
}