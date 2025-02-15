package com.modar.task.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.modar.task.R
import com.modar.task.base.BaseFragment
import com.modar.task.base.BaseItemUIState
import com.modar.task.base.BaseUiResource
import com.modar.task.base.Inflate
import com.modar.task.databinding.FragmentViewDataBinding
import com.modar.task.model.main.User
import com.modar.task.utils.Utils.showAlert
import com.modar.task.utils.Utils.toast
import com.modar.task.viewmodels.main.MainViewModel

class ViewDataFragment : BaseFragment<FragmentViewDataBinding, MainViewModel>() {
    override val inflate: Inflate<FragmentViewDataBinding>
        get() = FragmentViewDataBinding::inflate

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getInjectViewModel(): MainViewModel = mainViewModel

    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getAllUsers()
    }

    override fun initialization() {
        binding.apply {
            rvUsers.layoutManager = LinearLayoutManager(requireContext())
            rvUsers.adapter = usersAdapter
        }
    }

    override fun listeners() {
        binding.apply {
            usersAdapter.listener = object : UsersAdapter.UsersAdapterListener {
                override fun onItemClicked(value: User, position: Int) {
                    getString(R.string.dummy_taost, value.name).toast()
                }

                override fun onDeleteItemClicked(value: User, position: Int) {
                    mainViewModel.deleteUser(BaseItemUIState(value, position))
                }

                override fun checkIfIsEmpty(isEmpty: Boolean) {
                    binding.apply {
                        containerNoItems.isVisible = isEmpty
                        rvUsers.isVisible = !isEmpty
                        btnDeleteAll.visibility =
                            if (isEmpty) View.INVISIBLE else View.VISIBLE
                    }
                }
            }

            btnDeleteAll.setOnClickListener {
                requireContext().showAlert(
                    title = getString(R.string.delete_all),
                    message = getString(R.string.are_you_sure_you_want_to_delete_all_users),
                    canCancel = true,
                    textPositiveButton = getString(R.string.okay),
                    textNegativeButton = getString(R.string.cancel),
                    onPositiveClicked = {
                        mainViewModel.deleteALlUsers()
                    },
                )
            }

            btnBack.setOnClickListener {
                popFragment()
            }
        }
    }

    override fun initializeViewModel() {
        getAllUsersObserver()
        deleteUserObserver()
        deleteAllUsersObserver()
    }

    private fun deleteAllUsersObserver() {
        mainViewModel.deleteAllUsersLiveData.observe(viewLifecycleOwner) {
            when (it) {
                BaseUiResource.LoadingState -> {
                    binding.apply {
                        containerNoItems.isVisible = false
                        rvUsers.isVisible = false
                        pbLoader.isVisible = true
                    }
                }

                is BaseUiResource.FailureState -> {
                    binding.pbLoader.isVisible = false
                    (if (it.messageRes != null) getString(it.messageRes)
                    else getString(R.string.error_something_went_wrong)).toast()
                }

                is BaseUiResource.SuccessState -> {
                    binding.pbLoader.isVisible = false
                    usersAdapter.clearItems()
                }
            }
        }
    }

    private fun deleteUserObserver() {
        mainViewModel.deleteUserLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                BaseUiResource.LoadingState -> {}

                is BaseUiResource.FailureState -> {
                    (if (it.messageRes != null) getString(it.messageRes)
                    else getString(R.string.error_something_went_wrong)).toast()
                }

                is BaseUiResource.SuccessState -> {
                    it.uiState?.let { state ->
                        if (state.item is User) {
                            usersAdapter.setItemRemoved(state as BaseItemUIState<User>)
                        }
                    }
                }
            }
        }
    }

    private fun getAllUsersObserver() {
        mainViewModel.getAllUsersLiveData.observe(viewLifecycleOwner) {
            when (it) {
                BaseUiResource.LoadingState -> {
                    binding.apply {
                        containerNoItems.isVisible = false
                        rvUsers.isVisible = false
                        pbLoader.isVisible = true
                    }
                }

                is BaseUiResource.FailureState -> {
                    binding.pbLoader.isVisible = false
                    requireContext().showAlert(
                        title = getString(R.string.error),
                        message = if (it.messageRes != null) getString(it.messageRes)
                        else getString(R.string.error_something_went_wrong),
                        canCancel = false,
                        textPositiveButton = getString(R.string.retry),
                        textNegativeButton = getString(R.string.cancel),
                        onPositiveClicked = {
                            mainViewModel.getAllUsers()
                        },
                        onNegativeClicked = {
                            popFragment()
                        },
                    )
                }

                is BaseUiResource.SuccessState -> {
                    binding.apply {
                        pbLoader.isVisible = false
                        rvUsers.isVisible = true
                        usersAdapter.setItems(it.data ?: emptyList())
                    }
                }
            }
        }
    }

    private fun popFragment() {
        Navigation.findNavController(requireView()).popBackStack()
    }
}