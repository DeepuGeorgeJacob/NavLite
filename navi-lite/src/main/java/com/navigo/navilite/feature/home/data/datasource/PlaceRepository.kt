package com.navigo.navilite.feature.home.data.datasource

import com.navigo.navilite.feature.home.domain.model.Place
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class PlaceRepository @Inject constructor(private val placeDataSource: PlaceDataSource) {
    suspend fun getPlaces(): List<Place> {
        delay(500)
        return placeDataSource.getPlaces().map { place -> place.toDomain() }
    }

    suspend fun getPlaceById(id: Int): Place? {
        delay(500)
        return placeDataSource.getPlaceById(id)?.toDomain()
    }
}