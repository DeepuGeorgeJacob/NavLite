package com.navigo.navilite.feature.details.presentation

internal sealed interface DetailsSideEffect {
    data object CompleteNavigationSnackBar : DetailsSideEffect
}