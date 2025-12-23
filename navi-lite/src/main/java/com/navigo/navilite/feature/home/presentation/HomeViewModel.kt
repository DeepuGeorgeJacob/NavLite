package com.navigo.navilite.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navigo.navilite.core.NaviLiteStateContainer
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.domain.usecase.GetPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getPlaces: GetPlacesUseCase
) : ViewModel() {
    val container = NaviLiteStateContainer<HomeState, Unit>(
        initialState = HomeState(),
        scope = viewModelScope
    )

    init {
        fetchPlaceList()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectPlace -> navigateToPlaceDetails(event.id)
            else -> container.updateState { state -> state.copy(userActionState = HomeActionState.NoPendingAction) }
        }
    }

    private fun fetchPlaceList() {
        viewModelScope.launch {
            container.updateState { state ->
                state.copy(
                    userState = state.userState.copy(
                        isLoading = true,
                        isError = false
                    )
                )
            }
            getPlaces()
                .onSuccess { places ->
                    container.updateState { state ->
                        state.copy(
                            userState = state.userState.copy(
                                isLoading = false,
                                places = places
                            )
                        )
                    }
                }.onFailure {
                    container.updateState { state ->
                        state.copy(
                            userState = state.userState.copy(isLoading = false, isError = true)
                        )
                    }
                }
        }
    }

    private fun navigateToPlaceDetails(placeId: Id) {
        /**
         * Based on Manual Vivo's blog
         * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95#:~:text=projects.%20When%20the-,producer,-(the%20ViewModel)%20outlives
         */
        viewModelScope.launch {
            container.updateState { state ->
                state.copy(
                    userActionState = HomeActionState.NavigateToDetails(
                        placeId = placeId
                    )
                )
            }
        }
    }
}


