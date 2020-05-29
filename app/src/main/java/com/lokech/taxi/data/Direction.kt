package com.lokech.taxi.data

data class Direction(
    val routes: List<Route>
) {
    val line: String
        get() = routes.first().overview_polyline.points

    val distance: String
        get() = routes.first().legs.first().distance.text

    val duration: String
        get() = routes.first().legs.first().duration.text

    val startLatitude: Double
        get() = routes.first().legs.first().start_location.lat

    val startLongitude: Double
        get() = routes.first().legs.first().start_location.lng

    val endLatitude: Double
        get() = routes.first().legs.first().end_location.lat

    val endLongitude: Double
        get() = routes.first().legs.first().end_location.lng

    val startAddress: String
        get() = routes.first().legs.first().start_address

    val endAddress: String
        get() = routes.first().legs.first().end_address
}

data class Route(
    val legs: List<Leg>,
    val overview_polyline: Polyline
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: ShortLocation,
    val start_address: String,
    val start_location: ShortLocation

)

data class Distance(
    val text: String,
    val value: Long
)

data class Duration(
    val text: String,
    val value: Long
)

data class ShortLocation(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)