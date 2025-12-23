package com.navigo.navilite.feature.details.domain.usecase

import com.navigo.navilite.feature.home.domain.model.Coordinate
import com.navigo.navilite.feature.home.domain.model.Icon
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.domain.model.Name
import com.navigo.navilite.feature.home.domain.model.Place
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

internal class SimulationUseCaseTest {

    private val useCase = SimulationUseCase()

    @Test
    fun `invoke emits progress and then completed`() = runTest {
        val destination = Place(
            id = Id(1),
            name = Name("Test"),
            latitude = Coordinate(10.0),
            longitude = Coordinate(10.0),
            icon = Icon("icon")
        )

        val updates = useCase(destination).toList()

        assertTrue(updates.first() is SimulationUseCase.SimulationUpdate.Progress)
        assertTrue(updates.last() is SimulationUseCase.SimulationUpdate.Completed)
    }
}
