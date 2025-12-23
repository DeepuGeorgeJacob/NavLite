package com.navigo.navilite.feature.home.data.model

import com.navigo.navilite.feature.home.domain.model.Coordinate
import com.navigo.navilite.feature.home.domain.model.Icon
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.domain.model.Name
import com.navigo.navilite.feature.home.domain.model.Place

internal data class PlaceDto(
    val id: Int,
    val name: String,
    val lat: Double,
    val lng: Double,
    val icon: String
) {
    fun toDomain(): Place =
        Place(
            id = Id(id) ,
            name = Name(name) ,
            latitude = Coordinate(lat) ,
            longitude = Coordinate(lng),
            icon = Icon(icon)
        )
}

