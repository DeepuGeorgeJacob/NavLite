package com.navigo.navilite.feature.home.data.datasource

import com.navigo.navilite.feature.home.data.model.PlaceDto

internal interface PlaceDataSource {
    suspend fun getPlaces(): List<PlaceDto>
    suspend fun getPlaceById(id: Int): PlaceDto?
}