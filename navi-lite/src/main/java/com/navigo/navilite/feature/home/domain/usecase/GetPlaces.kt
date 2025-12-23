package com.navigo.navilite.feature.home.domain.usecase

import com.navigo.navilite.feature.home.data.datasource.PlaceRepository
import com.navigo.navilite.feature.home.domain.model.Place
import javax.inject.Inject

internal class GetPlacesUseCase @Inject constructor(private val repository: PlaceRepository) {

    suspend operator fun invoke(): Result<List<Place>> {
        return try {
            val places = repository.getPlaces()
            Result.success(places)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}