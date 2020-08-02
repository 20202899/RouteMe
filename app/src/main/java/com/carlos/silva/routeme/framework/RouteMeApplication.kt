package com.carlos.silva.routeme.framework

import android.app.Application
import com.carlos.silva.core.data.DirectionsDataRepository
import com.carlos.silva.core.interactors.GetDirections
import com.carlos.silva.routeme.R
import com.carlos.silva.routeme.framework.repository.RemoteDirectionsRepository
import com.google.android.libraries.places.api.Places;
import com.google.maps.GeoApiContext

class RouteMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val directionsDataRepository = DirectionsDataRepository(
            RemoteDirectionsRepository()
        )

        RouteMeViewModelFactory.inject(
            this,
            Interactors(
                GetDirections(directionsDataRepository)
            )
        )

        Places.initialize(this, getString(R.string.map_key))
    }
}