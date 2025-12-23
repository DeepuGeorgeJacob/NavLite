package com.navigo.navilite.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.navigo.navilite.core.NaviLiteStateContainer
import com.navigo.navilite.feature.details.domain.usecase.GetPlaceDetailsUseCase
import com.navigo.navilite.feature.details.domain.usecase.SimulationUseCase
import com.navigo.navilite.page.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val getPlaceDetails: GetPlaceDetailsUseCase,
    private val simulationUseCase: SimulationUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    val container = NaviLiteStateContainer<DetailsState, DetailsSideEffect>(
        initialState = DetailsState(),
        scope = viewModelScope
    )

    private val routeDetails = savedStateHandle.toRoute<Route.Details>()
    private val placeId = routeDetails.placeId
    private var simulationJob: Job? = null

    init {
        fetchPlaceDetails()
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.Retry -> fetchPlaceDetails()
            DetailsEvent.StartNavigation -> startNavigationSimulation()
        }
    }

    private fun fetchPlaceDetails() {
        viewModelScope.launch {
            container.updateState { it.copy(isLoading = true, isError = false) }
            getPlaceDetails(placeId)
                .onSuccess { place ->
                    container.updateState {
                        it.copy(
                            isLoading = false,
                            place = place,
                            isError = place == null
                        )
                    }
                }
                .onFailure {
                    container.updateState { it.copy(isLoading = false, isError = true) }
                }
        }
    }

    private fun startNavigationSimulation() {
        val destination = container.state.value.place ?: return
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            simulationUseCase(destination).collectLatest { update ->
                when (update) {
                    is SimulationUseCase.SimulationUpdate.Progress -> {
                        container.updateState {
                            it.copy(
                                navigationState = NavigationSimulationState.Navigating(
                                    currentLat = update.currentLat,
                                    currentLng = update.currentLng,
                                    remainingDistance = update.remainingDistance,
                                    estimatedTime = update.estimatedTime
                                )
                            )
                        }
                    }

                    SimulationUseCase.SimulationUpdate.Completed -> {
                        container.updateState { detailsState -> detailsState.copy(navigationState = NavigationSimulationState.Idle) }
                        container.sendSideEffect(DetailsSideEffect.CompleteNavigationSnackBar)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}
