package com.carlos.silva.core.domain

data class Place(
    val myLat: Double,
    val myLng: Double,
    val destinyLat: Double,
    val destinyLng: Double,
    val placeAddress: String,
    val placeId: Int
)