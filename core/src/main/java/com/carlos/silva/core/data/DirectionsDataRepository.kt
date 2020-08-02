package com.carlos.silva.core.data

class DirectionsDataRepository(private val directionsDataSource: DirectionsDataSource) {
    fun<T> getDirections(
        originLat: Double,
        originLng: Double,
        destinyLat: Double,
        destinyLng: Double
    ): T = directionsDataSource.get(
        originLat,
        originLng,
        destinyLat,
        destinyLng
    )
}