package com.navigo.navilite.feature.home.presentation

import app.cash.turbine.test
import com.navigo.navilite.feature.home.data.datasource.PlaceDataSource
import com.navigo.navilite.feature.home.data.datasource.PlaceRepository
import com.navigo.navilite.feature.home.data.model.PlaceDto
import com.navigo.navilite.feature.home.domain.model.Coordinate
import com.navigo.navilite.feature.home.domain.model.Icon
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.domain.model.Name
import com.navigo.navilite.feature.home.domain.model.Place
import com.navigo.navilite.feature.home.domain.usecase.GetPlacesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            HomeViewModel(getPlaces = GetPlacesUseCase(repository = PlaceRepository(MockDataSource())))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel.container.state.test {
            val initialState = HomeState(userState = HomeUserState(isLoading = true))
            assertEquals(initialState, awaitItem())
        }
    }

    @Test
    fun `verify state after data fetched`() = runTest {
        viewModel.container.state.test {
            // Initial state
            val initialState = HomeState(userState = HomeUserState(isLoading = true))
            assertEquals(initialState, awaitItem())

            // Updated state
            val updatedState = awaitItem()
            assertEquals(
                HomeState(
                    userState = HomeUserState(
                        isLoading = false, places = listOf(
                            Place(
                                id = Id(1),
                                name = Name("Home"),
                                latitude = Coordinate(59.3293),
                                longitude = Coordinate(18.0686),
                                icon = Icon("ic_home")
                            )
                        )
                    ),
                ),
                updatedState
            )
        }
    }

    @Test
    fun `verify state when data fetch fails`() = runTest {
        val errorDataSource = object : PlaceDataSource {
            override suspend fun getPlaces(): List<PlaceDto> = throw Exception("Error")
            override suspend fun getPlaceById(id: Int): PlaceDto? = null
        }
        val errorViewModel = HomeViewModel(GetPlacesUseCase(PlaceRepository(errorDataSource)))

        errorViewModel.container.state.test {
            skipItems(1)
            assertEquals(HomeState(userState = HomeUserState(isLoading = true)), awaitItem())
            assertEquals(
                HomeState(userState = HomeUserState(isLoading = false, isError = true)),
                awaitItem()
            )
        }
    }

    @Test
    fun `verify SelectPlace event updates action state`() = runTest {
        viewModel.container.state.test {
            // Skip initial states
            skipItems(2)

            val placeId = Id(1)
            viewModel.onEvent(HomeEvent.SelectPlace(placeId))

            val stateWithAction = awaitItem()
            assertEquals(
                HomeActionState.NavigateToDetails(placeId),
                stateWithAction.userActionState
            )
        }
    }

    @Test
    fun `verify NavigateToDetailsCompleted event clears action state`() = runTest {
        viewModel.container.state.test {
            // Skip initial states
            skipItems(2)

            // Trigger action
            val placeId = Id(1)
            viewModel.onEvent(HomeEvent.SelectPlace(placeId))
            awaitItem() // State with action

            // Clear action
            viewModel.onEvent(HomeEvent.NavigateToDetailsCompleted)
            val clearedState = awaitItem()
            assertEquals(
                HomeActionState.NoPendingAction,
                clearedState.userActionState
            )
        }
    }
}


private class MockDataSource : PlaceDataSource {
    override suspend fun getPlaces(): List<PlaceDto> =
        listOf(
            PlaceDto(1, "Home", 59.3293, 18.0686, "ic_home"),
        )

    override suspend fun getPlaceById(id: Int): PlaceDto? = null
}
