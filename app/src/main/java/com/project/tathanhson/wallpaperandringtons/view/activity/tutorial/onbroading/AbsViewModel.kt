package com.project.tathanhson.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class AbsViewModel<A, E, UI> : ViewModel() {

    val mAction: ViewModelAction<A> by lazy {
        ViewModelAction()
    }
    private val mEvent: ViewModelEvent<E> by lazy {
        ViewModelEvent()
    }
    private val mUiState: ViewModelUIState<UI> by lazy {
        ViewModelUIState(initUiState())
    }

    abstract fun initUiState(): UI

    protected inline fun <reified T : A> actionFlow() = mAction.actionFlow<T>()

    fun <T : A> dispatch(action: T) = mAction.dispatch(this, action)
    fun eventFlow() = mEvent.eventFlow()

    fun sendEvent(event: E) = mEvent.sendEvent(event)
    fun uiState(): UI = mUiState.stateValue()
    fun uiStateFlow(): Flow<UI> = mUiState.stateFlow()

    fun updateUI(function: (UI) -> UI) = mUiState.updateState(function)
}

class ViewModelAction<A> {
    val _action = MutableSharedFlow<A>()

    inline fun <reified T : A> actionFlow(): Flow<T> {
        return _action.filterIsInstance<T>()
    }

    private fun dispatch(scope: CoroutineScope, action: A) {
        scope.launch {
            delay(1)
            this@ViewModelAction._action.emit(action)
        }
    }

    fun dispatch(viewModel: ViewModel, action: A) {
        dispatch(viewModel.viewModelScope, action)
    }
}

class ViewModelEvent<T> {

    private val channel = Channel<T>(Channel.UNLIMITED)

    fun sendEvent(event: T) {
        channel.trySend(event)
    }

    fun eventFlow(): Flow<T> {
        return channel.receiveAsFlow()
    }
}

class ViewModelUIState<A>(value: A) {
    private val _action = MutableStateFlow<A>(value)

    fun stateFlow(): Flow<A> {
        return _action.asStateFlow()
    }

    fun stateValue(): A {
        return _action.value
    }

    fun updateState(function: (A) -> A) {
        _action.update(function)
    }
}


