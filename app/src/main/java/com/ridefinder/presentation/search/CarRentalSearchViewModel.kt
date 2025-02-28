package com.ridefinder.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * Created by fasil on 27/02/25.
 */
class CarRentalSearchViewModel : ViewModel() {
    private val _pickupLocation: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _pickupDate: MutableStateFlow<String?> = MutableStateFlow(null)

    val canSearch
        get() = combine(
            _pickupLocation,
            _pickupDate
        ) { pickupLocation, pickupDate ->
            pickupLocation.isNullOrEmpty().not() && pickupDate.isNullOrEmpty().not()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun updatePickupLocation(location: String) = _pickupLocation.update { location }

    fun updatePickupDate(date: String) = _pickupDate.update { date }


}