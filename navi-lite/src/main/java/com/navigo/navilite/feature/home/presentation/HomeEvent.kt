package com.navigo.navilite.feature.home.presentation

import com.navigo.navilite.feature.home.domain.model.Id

internal sealed interface HomeEvent {
    data class SelectPlace(val id: Id) : HomeEvent
    data object NavigateToDetailsCompleted : HomeEvent
}