package com.navigo.navilite.feature.details.domain.usecase

import com.navigo.navilite.feature.home.domain.model.Place
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class SimulationUseCase @Inject constructor() {

    sealed interface SimulationUpdate {
        data class Progress(
            val currentLat: Double,
            val currentLng: Double,
            val remainingDistance: String,
            val estimatedTime: String
        ) : SimulationUpdate
        data object Completed : SimulationUpdate
    }

    operator fun invoke(destination: Place): Flow<SimulationUpdate> = flow {
        var remainingDist = 5.0 // starting with 5.0 km
        
        while (remainingDist > 0) {
            val currentLat = destination.latitude.value - (remainingDist * 0.01)
            val currentLng = destination.longitude.value - (remainingDist * 0.01)
            
            emit(
                SimulationUpdate.Progress(
                    currentLat = currentLat,
                    currentLng = currentLng,
                    remainingDistance = String.format("%.2f km", remainingDist),
                    estimatedTime = String.format("%d min", (remainingDist * 2).toInt())
                )
            )
            
            delay(1000)
            remainingDist -= 0.5 // simulated speed (0.5km per second for demo)
            if (remainingDist < 0) remainingDist = 0.0
        }
        
        emit(SimulationUpdate.Completed)
    }
}
