package com.navigo.navilite.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Inspired from https://orbit-mvi.org/
 */
internal class NaviLiteStateContainer<STATE, SIDE_EFFECT>(
    initialState: STATE,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SIDE_EFFECT>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun updateState(reduce: (STATE) -> STATE) {
        _state.update { reduce(it) }
    }

    fun sendSideEffect(se: SIDE_EFFECT) {
        scope.launch { _sideEffect.emit(se) }
    }
}