package com.navigo.navilite.feature.home.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.navigo.navilite.feature.home.data.model.PlaceDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PlaceDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val gson: Gson
) : PlaceDataSource {
    override suspend fun getPlaces(): List<PlaceDto> {
        val jsonString = context.assets.open("places.json").bufferedReader().use { it.readText() }
        return gson.fromJson(jsonString, object : TypeToken<List<PlaceDto>>() {}.type)
    }

    override suspend fun getPlaceById(id: Int): PlaceDto? {
        return getPlaces().find { it.id == id }
    }
}