package com.lokech.taxi.data

data class Direction(
    val routes: List<Route>
) {
    val line: String
        get() = routes.first().overview_polyline.points
}

data class Route(
    val overview_polyline: Polyline
)

data class Polyline(
    val points: String
)