package com.lokech.taxi.data


data class PlaceTextSearch(
    val results: List<Result>
)

val PlaceTextSearch.places: List<Place>
    get() = results.map { it.getPlace() }


data class Result(
    val id: String,
    val formatted_address: String?,
    val geometry: Geometry,
    val icon: String,
    val name: String
) {
    fun getPlace() = Place(
        id = id,
        name = name,
        icon = icon,
        address = formatted_address ?: "Unknown Address",
        latitude = geometry.location.lat,
        longitude = geometry.location.lng
    )
}

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)