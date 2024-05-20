package com.project.tathanhson.myapplication


class OnBroadViewModel :
    AbsViewModel<OnBroadAction, OnBroadEvent, OnBroadUIState>() {

    override fun initUiState(): OnBroadUIState = OnBroadUIState()

    init {
        initHandleChangePosition()
        initHandleChangeScale()
        initHandleChangeDragging()
    }

    private fun initHandleChangeScale() {
        actionFlow<OnBroadAction.ChangeScale>()
            .collectLatestIn(this) { action ->
                updateUI {
                    it.copy(
                        scale = action.scale,
                        toPosition = action.toPosition
                    )
                }
            }
    }

    private fun initHandleChangeDragging() {
        actionFlow<OnBroadAction.ChangeDragging>()
            .collectLatestIn(this) { action ->
                updateUI {
                    it.copy(isDragging = action.dragging)
                }
            }
    }

    private fun initHandleChangePosition() {
        actionFlow<OnBroadAction.ChangePosition>()
            .collectLatestIn(this) { action ->
                updateUI {
                    it.copy(
                        position = action.position,
                        toPosition = action.position
                    )
                }
            }
    }
}