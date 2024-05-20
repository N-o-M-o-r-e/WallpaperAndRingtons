package com.project.tathanhson.myapplication

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.launchCollectLatestRepeatWithState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@launchCollectLatestRepeatWithState.collectLatest {
                runBlock(it)
            }
        }
    }
}

inline fun <T> Flow<T>.launchCollectRepeatWithState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@launchCollectRepeatWithState.collect {
                runBlock(it)
            }
        }
    }
}

inline fun <T> Flow<T>.launchCollectLatestStarted(
    fragment: Fragment,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectLatestRepeatWithState(
        lifecycleOwner = fragment.viewLifecycleOwner,
        state = Lifecycle.State.STARTED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectLatestCreated(
    fragment: Fragment,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectLatestRepeatWithState(
        lifecycleOwner = fragment.viewLifecycleOwner,
        state = Lifecycle.State.CREATED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectLatestResumed(
    fragment: Fragment,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectLatestRepeatWithState(
        lifecycleOwner = fragment.viewLifecycleOwner,
        state = Lifecycle.State.RESUMED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectStarted(
    fragment: Fragment,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectRepeatWithState(
        lifecycleOwner = fragment.viewLifecycleOwner,
        state = Lifecycle.State.STARTED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectCreated(
    fragment: Fragment,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectRepeatWithState(
        lifecycleOwner = fragment.viewLifecycleOwner,
        state = Lifecycle.State.CREATED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectResumed(
    fragment: Fragment,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectRepeatWithState(
        lifecycleOwner = fragment.viewLifecycleOwner,
        state = Lifecycle.State.RESUMED,
        runBlock = runBlock
    )
}

/*Activity Collect*/
inline fun <T> Flow<T>.launchCollectLatestStarted(
    activity: ComponentActivity,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectLatestRepeatWithState(
        lifecycleOwner = activity,
        state = Lifecycle.State.STARTED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectLatestCreated(
    activity: ComponentActivity,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectLatestRepeatWithState(
        lifecycleOwner = activity,
        state = Lifecycle.State.CREATED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectLatestResumed(
    activity: ComponentActivity,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectLatestRepeatWithState(
        lifecycleOwner = activity,
        state = Lifecycle.State.RESUMED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectStarted(
    activity: ComponentActivity,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectRepeatWithState(
        lifecycleOwner = activity,
        state = Lifecycle.State.STARTED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectCreated(
    activity: ComponentActivity,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectRepeatWithState(
        lifecycleOwner = activity,
        state = Lifecycle.State.CREATED,
        runBlock = runBlock
    )
}

inline fun <T> Flow<T>.launchCollectResumed(
    activity: ComponentActivity,
    crossinline runBlock: suspend (data: T) -> Unit
): Job {
    return this.launchCollectRepeatWithState(
        lifecycleOwner = activity,
        state = Lifecycle.State.RESUMED,
        runBlock = runBlock
    )
}

/*ViewModel*/
inline fun <T> Flow<T>.collectIn(
    viewModel: ViewModel,
    crossinline collectionBlock: suspend (data: T) -> Unit
): Job {
    return viewModel.viewModelScope.launch {
        this@collectIn.collect {
            collectionBlock(it)
        }
    }
}

inline fun <T> Flow<T>.collectLatestIn(
    viewModel: ViewModel,
    crossinline collectionBlock: suspend (data: T) -> Unit
): Job {
    return viewModel.viewModelScope.launch {
        this@collectLatestIn.collectLatest {
            collectionBlock(it)
        }
    }
}

inline fun <T> Flow<T>.collectFirstIn(
    viewModel: ViewModel,
    crossinline collectionBlock: suspend (data: T) -> Unit
): Job {
    return this@collectFirstIn.conflate().onEach {
        collectionBlock(it)
    }.launchIn(viewModel.viewModelScope)
}

/*Fragment launch When (repeat)*/
inline fun Fragment.launchWhenCreated(crossinline block: () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

inline fun Fragment.launchWhenResumed(crossinline block: () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

inline fun Fragment.launchWhenStarted(crossinline block: () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

/*Activity launch when(repeat)*/
inline fun ComponentActivity.launchWhenCreated(crossinline block: () -> Unit): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

inline fun ComponentActivity.launchWhenResumed(crossinline block: () -> Unit): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

inline fun ComponentActivity.launchWhenStarted(crossinline block: () -> Unit): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

/*Fragment launch In*/
inline fun Fragment.launchInCreated(crossinline block: () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.withCreated(block)
    }
}

inline fun Fragment.launchInResumed(crossinline block: () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.withResumed(block)
    }
}

inline fun Fragment.launchInStarted(crossinline block: () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.withStarted(block)
    }
}
/*Activity launch In*/

inline fun ComponentActivity.launchInCreated(crossinline block: () -> Unit): Job {
    return lifecycleScope.launch {
        withCreated(block)
    }
}

inline fun ComponentActivity.launchInResumed(crossinline block: () -> Unit): Job {
    return lifecycleScope.launch {
        withResumed(block)
    }
}

inline fun ComponentActivity.launchInStarted(crossinline block: () -> Unit): Job {
    return lifecycleScope.launch {
        withStarted(block)
    }
}

/*Suspend to flow*/
inline fun <reified T> flowOfSuspend(crossinline function: suspend () -> T): Flow<T> {
    return flow<T> {
        coroutineScope {
            emit(function())
        }
    }
}