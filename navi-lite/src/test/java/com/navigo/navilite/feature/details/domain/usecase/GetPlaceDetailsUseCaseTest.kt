package com.navigo.navilite.feature.details.domain.usecase

import com.navigo.navilite.feature.home.data.datasource.PlaceDataSource
import com.navigo.navilite.feature.home.data.datasource.PlaceRepository
import com.navigo.navilite.feature.home.data.model.PlaceDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class GetPlaceDetailsUseCaseTest {

    private lateinit var useCase: GetPlaceDetailsUseCase
    private val mockDataSource = object : PlaceDataSource {
        override suspend fun getPlaces(): List<PlaceDto> = emptyList()
        override suspend fun getPlaceById(id: Int): PlaceDto? {
            return if (id == 1) PlaceDto(1, "Test Place", 1.0, 1.0, "icon") else null
        }
    }

    @Before
    fun setUp() {
        useCase = GetPlaceDetailsUseCase(PlaceRepository(mockDataSource))
    }

    @Test
    fun `invoke returns success when repository returns place`() = runTest {
        val result = useCase(1)
        assertTrue(result.isSuccess)
        assertEquals("Test Place", result.getOrNull()?.name?.value)
    }

    @Test
    fun `invoke returns success with null when repository returns null`() = runTest {
        val result = useCase(2)
        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull())
    }
}
