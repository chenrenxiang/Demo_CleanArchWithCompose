package com.clean.architecture.demo.common

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.clean.architecture.demo.app.Page
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * for people who are too lazy to declare uiState/uiEvent in each ViewModel
  */
abstract class StateEventViewModel<STATE>(initialState: STATE): ViewModel() {

    // uiState
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    fun updateState(reducer: (STATE) -> STATE) {
        _uiState.update(reducer)
    }

    // one-off event
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun sendEvent(event: UiEvent) {
        _uiEvent.emit(event)
    }

    fun sendEventWithViewModelScope(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

}


/**
 * Safely collects values from Flow in a lifecycle-aware manner.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectWithLifecycle(
    key: Any? = Unit,
    collector: (T) -> Unit
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectWithLifecycle.collect(collector)
        }
    }
}


sealed class UiEvent {
    data class Toast(val msg: String): UiEvent()
    data class Navigate(val targetPage: Page): UiEvent()
}
