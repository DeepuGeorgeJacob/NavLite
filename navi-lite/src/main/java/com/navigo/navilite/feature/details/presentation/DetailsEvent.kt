package com.navigo.navilite.feature.details.presentation

internal sealed interface DetailsEvent {
    data object Retry : DetailsEvent
    data object StartNavigation : DetailsEvent
}