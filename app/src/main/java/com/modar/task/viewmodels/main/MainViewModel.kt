package com.modar.task.viewmodels.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.modar.task.R
import com.modar.task.base.BaseItemUIState
import com.modar.task.base.BaseRoomResource
import com.modar.task.base.BaseUiResource
import com.modar.task.base.BaseViewModel
import com.modar.task.model.main.User
import com.modar.task.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val insertUserResponse = MutableLiveData<BaseUiResource<Unit>>()
    val insertUserLiveData: LiveData<BaseUiResource<Unit>>
        get() = insertUserResponse

    private val getAllUsersResponse = MutableLiveData<BaseUiResource<List<User>>>()
    val getAllUsersLiveData: LiveData<BaseUiResource<List<User>>>
        get() = getAllUsersResponse

    private val deleteUserResponse = MutableLiveData<BaseUiResource<Unit>>()
    val deleteUserLiveData: LiveData<BaseUiResource<Unit>>
        get() = deleteUserResponse

    private val deleteAllUsersResponse = MutableLiveData<BaseUiResource<Unit>>()
    val deleteAllUsersLiveData: LiveData<BaseUiResource<Unit>>
        get() = deleteAllUsersResponse

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            handleRoomRequest(
                R.id.insert_user,
                {
                    userRepository.insert(user)
                },
                BaseItemUIState(user)
            )
        }
    }

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            handleRoomRequest(
                R.id.get_all_users,
                {
                    userRepository.getAllUsers()
                },
                null
            )
        }
    }

    fun deleteALlUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            handleRoomRequest(
                R.id.delete_all_users,
                {
                    userRepository.deleteAllUsers()
                },
                null
            )
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            handleRoomRequest(
                R.id.delete_user,
                {
                    userRepository.delete(user)
                },
                BaseItemUIState(user)
            )
        }
    }

    override fun <T> onSuccessfulResponse(
        id: Int,
        resource: BaseRoomResource<T>?,
        uiState: BaseItemUIState<Any>?
    ) {
        when (id) {
            R.id.insert_user -> {
                insertUserResponse.value = BaseUiResource.SuccessState(
                    data = resource?.response as Unit,
                    uiState = uiState,
                )
            }

            R.id.get_all_users -> {
                getAllUsersResponse.value = BaseUiResource.SuccessState(
                    data = resource?.response as List<User>,
                    uiState = uiState,
                )
            }

            R.id.delete_user -> {
                deleteUserResponse.value = BaseUiResource.SuccessState(
                    data = resource?.response as Unit,
                    uiState = uiState,
                )
            }

            R.id.delete_all_users -> {
                deleteAllUsersResponse.value = BaseUiResource.SuccessState(
                    data = resource?.response as Unit,
                    uiState = uiState,
                )
            }
        }
    }

    override fun onFailedResponse(
        id: Int,
        errorStringRes: Int?,
        uiState: BaseItemUIState<Any>?
    ) {
        when (id) {
            R.id.insert_user -> {
                insertUserResponse.value = BaseUiResource.FailureState(
                    messageRes = errorStringRes,
                    uiState = uiState,
                )
            }

            R.id.get_all_users -> {
                getAllUsersResponse.value = BaseUiResource.FailureState(
                    messageRes = errorStringRes,
                    uiState = uiState,
                )
            }

            R.id.delete_user -> {
                deleteUserResponse.value = BaseUiResource.FailureState(
                    messageRes = errorStringRes,
                    uiState = uiState,
                )
            }

            R.id.delete_all_users -> {
                deleteAllUsersResponse.value = BaseUiResource.FailureState(
                    messageRes = errorStringRes,
                    uiState = uiState,
                )
            }
        }
    }
}