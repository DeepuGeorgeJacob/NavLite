package com.navigo.navilite.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.navigo.navilite.feature.details.domain.usecase.GetPlaceDetailsUseCase
import com.navigo.navilite.feature.details.domain.usecase.SimulationUseCase
import com.navigo.navilite.feature.home.data.datasource.PlaceDataSource
import com.navigo.navilite.feature.home.data.datasource.PlaceRepository
import com.navigo.navilite.feature.home.data.model.PlaceDto
import com.navigo.navilite.page.navigation.Route
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockPlaceDto = PlaceDto(1, "Test Place", 10.0, 10.0, "icon")

    private val mockDataSource = object : PlaceDataSource {
        override suspend fun getPlaces(): List<PlaceDto> = listOf(mockPlaceDto)
        override suspend fun getPlaceById(id: Int): PlaceDto? = if (id == 1) mockPlaceDto else null
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun getMockSavedState(placeId: Int): SavedStateHandle {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        val mockSavedStateHandle = mockk<SavedStateHandle>()
        every {
            mockSavedStateHandle.toRoute<Route.Details>()
        } returns Route.Details(placeId = placeId)
        return mockSavedStateHandle
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state fetches place details successfully`() = runTest {
        val savedStateHandle = getMockSavedState(1)
        val repository = PlaceRepository(mockDataSource)
        val detailsViewModel = DetailsViewModel(
            getPlaceDetails = GetPlaceDetailsUseCase(repository),
            simulationUseCase = SimulationUseCase(),
            savedStateHandle = savedStateHandle
        )
        detailsViewModel.container.state.test {
            skipItems(1)
            assertEquals(true, awaitItem().isLoading)
            val loadedState = awaitItem()
            assertEquals(false, loadedState.isLoading)
            assertEquals("Test Place", loadedState.place?.name?.value)
            assertEquals(false, loadedState.isError)
        }
    }

    @Test
    fun `verify state when place details fetch fails`() = runTest {
        val errorDataSource = object : PlaceDataSource {
            override suspend fun getPlaces(): List<PlaceDto> = emptyList()
            override suspend fun getPlaceById(id: Int): PlaceDto = throw Exception("Error")
        }
        val errorViewModel = DetailsViewModel(
            getPlaceDetails = GetPlaceDetailsUseCase(PlaceRepository(errorDataSource)),
            simulationUseCase = SimulationUseCase(),
            savedStateHandle = getMockSavedState(placeId = 2)
        )

        errorViewModel.container.state.test {
            skipItems(1)
            assertTrue(awaitItem().isLoading)
            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals(true, errorState.isError)
        }
    }

    @Test
    fun `StartNavigation event updates navigation state to Navigating`() = runTest {
        val savedStateHandle = getMockSavedState(1)
        val repository = PlaceRepository(mockDataSource)
        val detailsViewModel = DetailsViewModel(
            getPlaceDetails = GetPlaceDetailsUseCase(repository),
            simulationUseCase = SimulationUseCase(),
            savedStateHandle = savedStateHandle
        )
        detailsViewModel.container.state.test {
            skipItems(3)

            detailsViewModel.onEvent(DetailsEvent.StartNavigation)

            val navigatingState = awaitItem()
            assertTrue(navigatingState.navigationState is NavigationSimulationState.Navigating)
        }
    }
}
