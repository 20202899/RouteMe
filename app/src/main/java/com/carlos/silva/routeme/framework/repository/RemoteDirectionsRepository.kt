package com.carlos.silva.routeme.framework.repository

import com.carlos.silva.core.data.DirectionsDataRepository
import com.carlos.silva.core.data.DirectionsDataSource
import com.carlos.silva.routeme.framework.GeoApiContextSingleton
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.model.LatLng
import com.google.maps.model.TrafficModel
import com.google.maps.model.TransitMode
import com.google.maps.model.TravelMode
import org.intellij.lang.annotations.Language
import java.lang.IllegalStateException

class RemoteDirectionsRepository : DirectionsDataSource {
    override fun<T> get(
        originLat: Double,
        originLng: Double,
        destinyLat: Double,
        destinyLng: Double
    ): T {
        val directionsApi = DirectionsApi.newRequest(GeoApiContextSingleton.getApiContext())
        return directionsApi
            .origin(LatLng(originLat, originLng))
            .destination(LatLng(destinyLat, destinyLng))
            .mode(TravelMode.TRANSIT)
            .departureTimeNow()
            .language("pt_br")
            .transitMode(TransitMode.BUS)
            .trafficModel(TrafficModel.BEST_GUESS)
            .await() as T
    }

}