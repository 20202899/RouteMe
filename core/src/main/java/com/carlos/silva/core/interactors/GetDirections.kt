package com.carlos.silva.core.interactors

import com.carlos.silva.core.data.DirectionsDataRepository

class GetDirections (private val directionsDataRepository: DirectionsDataRepository) {
    fun<T> invoke(
        originLat: Double,
        originLng: Double,
        destinyLat: Double,
        destinyLng: Double
    ): T = directionsDataRepository.getDirections(
        originLat,
        originLng,
        destinyLat,
        destinyLng
    )
}