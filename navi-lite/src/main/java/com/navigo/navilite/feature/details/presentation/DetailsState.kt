package com.navigo.navilite.feature.details.presentation

import androidx.compose.runtime.Immutable
import com.navigo.navilite.feature.home.domain.model.Place

@Immutable
internal data class DetailsState(
    val isLoading: Boolean = false,
    val place: Place? = null,
    val isError: Boolean = false,
    val navigationState: NavigationSimulationState = NavigationSimulationState.Idle,
)

@Immutable
internal sealed interface NavigationSimulationState {
    data object Idle : NavigationSimulationState
    data class Navigating(
        val currentLat: Double,
        val currentLng: Double,
        val remainingDistance: String,
        val estimatedTime: String
    ) : NavigationSimulationState
}
