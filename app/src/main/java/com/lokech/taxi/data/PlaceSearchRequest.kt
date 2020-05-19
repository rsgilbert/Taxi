package com.lokech.taxi.data

data class PlaceSearchRequest(
    val candidates: List<Place>,
    val status: String
)
