package com.modar.task.base

sealed class BaseUiResource<out T> {
    data object LoadingState : BaseUiResource<Nothing>()

    data class FailureState(
        val messageRes: Int? = null,
        val uiState: BaseItemUIState<Any>? = null,
    ) : BaseUiResource<Nothing>()

    data class SuccessState<T>(
        val data: T? = null,
        val uiState: BaseItemUIState<Any>? = null,
    ) : BaseUiResource<T>()
}
