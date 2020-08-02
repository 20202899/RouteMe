package com.carlos.silva.routeme.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.maps.GeoApiContext

open class RouteMeViewModel (
    private val application: Application,
    private val interactors: Interactors
) : ViewModel() {
    val context: RouteMeApplication
        get() = application as RouteMeApplication
}