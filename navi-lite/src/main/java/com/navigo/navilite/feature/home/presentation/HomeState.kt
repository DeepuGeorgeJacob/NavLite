package com.navigo.navilite.feature.home.presentation

import androidx.compose.runtime.Immutable
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.domain.model.Place

internal data class HomeState(
    val userState: HomeUserState = HomeUserState(),
    val userActionState: HomeActionState = HomeActionState.NoPendingAction
)

@Immutable
internal data class HomeUserState(
    val isLoading: Boolean = false,
    val places: List<Place> = emptyList(),
    val isError: Boolean = false
)

/**
 * Based on Manual Vivo's blog
 * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95#:~:text=projects.%20When%20the-,producer,-(the%20ViewModel)%20outlives
 */

internal sealed interface HomeActionState {
    data object NoPendingAction : HomeActionState
    data class NavigateToDetails(val placeId: Id) : HomeActionState
}
