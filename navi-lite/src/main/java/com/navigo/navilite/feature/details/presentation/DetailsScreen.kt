package com.navigo.navilite.feature.details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.navigo.navilite.R
import com.navigo.navilite.feature.details.presentation.composible.PlaceHeader
import com.navigo.navilite.feature.details.presentation.composible.StartNavigationButton
import com.navigo.navilite.feature.home.domain.model.Place
import com.navigo.ui.components.SimpleInformationBox

@Composable
internal fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect by viewModel.container.sideEffect.collectAsStateWithLifecycle(initialValue = null)

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sideEffect) {
        if (sideEffect is DetailsSideEffect.CompleteNavigationSnackBar) {
            snackBarHostState.showSnackbar(context.getString(R.string.destination_reached_message))
        }
    }

    DetailsScreenContent(
        state = state,
        snackBarHostState = snackBarHostState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun DetailsScreenContent(
    state: DetailsState,
    snackBarHostState: SnackbarHostState,
    onEvent: (DetailsEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.isError) {
            Text(
                text = stringResource(R.string.error_loading_places),
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            state.place?.let { place ->
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        PlaceHeader(place = place)
                        PlaceInformation(
                            place = place,
                            navigationState = state.navigationState
                        )
                    }
                    StartNavigationButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onStartNavigation = { onEvent(DetailsEvent.StartNavigation) },
                        isNavigating = state.navigationState is NavigationSimulationState.Navigating
                    )
                }
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PlaceInformation(
    place: Place,
    navigationState: NavigationSimulationState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.location),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SimpleInformationBox(
            label = stringResource(R.string.latitude),
            value = place.latitude.value.toString()
        )
        Spacer(modifier = Modifier.height(12.dp))
        SimpleInformationBox(
            label = stringResource(R.string.longitude),
            value = place.longitude.value.toString()
        )

        if (navigationState is NavigationSimulationState.Navigating) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.navigation_simulation),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SimpleInformationBox(
                label = stringResource(R.string.current_latitude),
                value = String.format("%.6f", navigationState.currentLat)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SimpleInformationBox(
                label = stringResource(R.string.current_longitude),
                value = String.format("%.6f", navigationState.currentLng)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SimpleInformationBox(
                label = stringResource(R.string.distance),
                value = navigationState.remainingDistance
            )
            Spacer(modifier = Modifier.height(12.dp))
            SimpleInformationBox(
                label = stringResource(R.string.estimated_time),
                value = navigationState.estimatedTime
            )
        }
    }
}
