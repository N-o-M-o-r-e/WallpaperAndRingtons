package com.project.tathanhson.myapplication

sealed interface OnBroadAction {

    data class ChangePosition(val position: Int) : OnBroadAction

    data class ChangeScale(val scale: Float, val toPosition: Int) : OnBroadAction

    data class ChangeDragging(val dragging: Boolean) : OnBroadAction

}

sealed interface OnBroadEvent

data class OnBroadUIState(
    val position: Int = 0,
    val toPosition: Int = 0,
    val scale: Float = 1f,
    val isDragging: Boolean = false
)