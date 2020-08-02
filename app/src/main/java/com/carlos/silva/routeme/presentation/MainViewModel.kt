package com.carlos.silva.routeme.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carlos.silva.routeme.framework.Interactors
import com.carlos.silva.routeme.framework.RouteMeViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.maps.model.DirectionsResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val interactors: Interactors
) : RouteMeViewModel(application, interactors) {
    val placeLiveData = MutableLiveData<Place>()
    val myLocationLiveData = MutableLiveData<LatLng>()

    fun<T> getDirections(
        originLat: Double,
        originLng: Double,
        destinyLat: Double,
        destinyLng: Double
    ) = interactors.getDirections.invoke<T>(
        originLat,
        originLng,
        destinyLat,
        destinyLng
    )
}