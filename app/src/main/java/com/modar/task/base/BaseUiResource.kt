package com.modar.task.base

sealed class BaseUiResource<out T> {
    data object LoadingState : BaseUiResource<Nothing>()

    data class FailureState(
        val message: String? = null,
        val errors: Map<String, List<String>>? = null,
        val uiState: BaseItemUIState<Any>? = null,
    ) : BaseUiResource<Nothing>()

    data class SuccessState<T>(
        val message: String? = null,
        val data: T? = null,
        val uiState: BaseItemUIState<Any>? = null,
    ) : BaseUiResource<T>()
}
