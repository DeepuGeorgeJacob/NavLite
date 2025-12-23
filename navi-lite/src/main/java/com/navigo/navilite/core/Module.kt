package com.navigo.navilite.core

import com.google.gson.Gson
import com.navigo.navilite.feature.home.data.datasource.PlaceDataSource
import com.navigo.navilite.feature.home.data.datasource.PlaceDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class Module {

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }


    @Binds
    @Singleton
    abstract fun bindPlaceDataSource(placeDataSourceImpl: PlaceDataSourceImpl): PlaceDataSource
}