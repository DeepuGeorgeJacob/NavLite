package com.navigo.navilite.feature.home.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.domain.model.Place
import com.navigo.ui.R

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetails: (id: Id) -> Unit
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val onPlaceClick = remember(viewModel) {
        { id: Id -> viewModel.onEvent(HomeEvent.SelectPlace(id)) }
    }
    LaunchedEffect(state.userActionState) {
        when (val action = state.userActionState) {
            is HomeActionState.NavigateToDetails -> {
                onNavigateToDetails(action.placeId)
                viewModel.onEvent(event = HomeEvent.NavigateToDetailsCompleted)
            }

            HomeActionState.NoPendingAction -> {}
        }
    }
    HomeScreenView(state = state.userState, onClick = onPlaceClick)
}

@Composable
internal fun HomeScreenView(state: HomeUserState, onClick: (Id) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.places, key = { it.id }) { place ->
                    val onItemClick = remember(place.id, onClick) {
                        { onClick(place.id) }
                    }
                    PlaceItem(place = place, onClick = onItemClick)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun PlaceItem(place: Place, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(place.name.value) },
        supportingContent = {
            Column {
                Text(
                    stringResource(
                        com.navigo.navilite.R.string.latitude_value,
                        place.latitude.getFormattedValue()
                    )
                )
                Text(
                    stringResource(
                        com.navigo.navilite.R.string.longitude_value,
                        place.longitude.getFormattedValue()
                    )
                )
            }
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    )
}
