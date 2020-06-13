package com.lokech.taxi.data


data class Search(
    val searchWord: String = "",
    val places: List<Place> = emptyList()
)