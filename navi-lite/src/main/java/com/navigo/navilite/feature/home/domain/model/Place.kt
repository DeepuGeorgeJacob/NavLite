package com.navigo.navilite.feature.home.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
internal data class Place(
    val id: Id,
    val name: Name,
    val latitude: Coordinate,
    val longitude: Coordinate,
    val icon: Icon
)

@JvmInline
@Parcelize
internal value class Id(val value: Int) : Parcelable

@JvmInline
internal value class Name(val value: String)

@JvmInline
internal value class Coordinate(val value: Double) {
    fun getFormattedValue(): String = value.toString();
}

@JvmInline
internal value class Icon(val value: String)