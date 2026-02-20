package com.clean.architecture.demo.ui.feature.home

import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.StateEventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class HomeUiState(
    val apiRequest: State<Unit> = State.Idle,
)

@HiltViewModel
class HomeViewModel @Inject constructor()
    : StateEventViewModel<HomeUiState>(HomeUiState()) {

        //just an example

}