package com.carlos.silva.core.data

interface DirectionsDataSource {
    fun<T> get(
        originLat: Double,
        originLng: Double,
        destinyLat: Double,
        destinyLng: Double
    ): T
}