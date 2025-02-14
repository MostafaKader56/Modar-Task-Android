package com.modar.task.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modar.task.R
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Base Class for ViewModels in the project where it's logic differ from project to another
@ViewModelScoped
abstract class BaseViewModel : ViewModel() {
    protected val startLoading = MutableLiveData<Boolean>()
    val startLoadingLiveData: LiveData<Boolean>
        get() = startLoading

    suspend fun <T : Any> handleRoomRequest(
        id: Int,
        request: suspend () -> T?,
        uiState: BaseItemUIState<Any>? = null,
    ) {
        // Here Can contain more complexity with various logic types.
        // But now it's very simple cause this is the wanted logic.
        val response = request.invoke()
        if (response != null) {
            uiState?.success = true
            withContext(Dispatchers.Main) {
                onSuccessfulResponse(id, BaseRoomResource(response), uiState)
            }
        } else {
            uiState?.success = false
            uiState?.errorMessageRes = R.string.error_something_went_wrong
            withContext(Dispatchers.Main) {
                onFailedResponse(id, R.string.error_something_went_wrong, uiState)
            }
        }
    }

    abstract fun <T> onSuccessfulResponse(
        id: Int, resource: BaseRoomResource<T>?, uiState: BaseItemUIState<Any>? = null
    )

    abstract fun onFailedResponse(
        id: Int, errorStringRes: Int?, uiState: BaseItemUIState<Any>? = null
    )
}