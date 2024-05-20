package com.project.tathanhson.myapplication

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.abs

fun View.showSnackbar(content: String, time: Int = Snackbar.LENGTH_SHORT) {
    CoroutineScope(Dispatchers.Main).launch {
        Snackbar.make(this@showSnackbar, content, time).show()
    }
}

fun View.showSnackbar(content: String, long: Boolean = false) {
    showSnackbar(
        content,
        time = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    )
}


private var clickTime = 0L
fun View.onSingleClickListener(blockTime: Int = 200, onClick: () -> Unit) {
    this.setOnClickListener {
        System.currentTimeMillis().takeIf { abs(it - clickTime) > blockTime }?.run {
            onClick()
            clickTime = this
        }
    }
}

sealed interface OnScroll {
    data class ChangeState(val newState: Int) : OnScroll
    data class Scrolled(val state: Int, val dx: Int, val dy: Int) : OnScroll
}


fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(
    kotlinx.coroutines.flow.combine(flow, flow2, flow3, ::Triple),
    kotlinx.coroutines.flow.combine(flow4, flow5, flow6, ::Triple)
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(
    kotlinx.coroutines.flow.combine(flow, flow2, flow3, ::Triple),
    kotlinx.coroutines.flow.combine(flow4, flow5, flow6, ::Triple),
    flow7
) { t1, t2, t3 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third,
        t3,
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(
    kotlinx.coroutines.flow.combine(flow, flow2, flow3, ::Triple),
    kotlinx.coroutines.flow.combine(flow4, flow5, flow6, ::Triple),
    kotlinx.coroutines.flow.combine(flow7, flow8, ::Pair)
) { t1, t2, t3 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third,
        t3.first,
        t3.second
    )
}