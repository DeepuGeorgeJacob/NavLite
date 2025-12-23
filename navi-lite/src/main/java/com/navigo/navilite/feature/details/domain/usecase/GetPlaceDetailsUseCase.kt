package com.navigo.navilite.feature.details.domain.usecase

import com.navigo.navilite.feature.home.data.datasource.PlaceRepository
import com.navigo.navilite.feature.home.domain.model.Place
import javax.inject.Inject

internal class GetPlaceDetailsUseCase @Inject constructor(private val repository: PlaceRepository) {

    suspend operator fun invoke(id: Int): Result<Place?> {
        return try {
            val place = repository.getPlaceById(id)
            Result.success(place)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}