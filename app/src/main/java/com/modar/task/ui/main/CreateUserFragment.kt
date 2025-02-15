package com.modar.task.ui.main

import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.modar.task.R
import com.modar.task.base.BaseFragment
import com.modar.task.base.BaseUiResource
import com.modar.task.base.Inflate
import com.modar.task.databinding.FragmentCreateUserBinding
import com.modar.task.enums.EditTextType
import com.modar.task.model.main.Gender
import com.modar.task.model.main.User
import com.modar.task.ui.component.SpinnerItemsAdapter
import com.modar.task.utils.Utils.toast
import com.modar.task.viewmodels.main.MainViewModel

class CreateUserFragment : BaseFragment<FragmentCreateUserBinding, MainViewModel>() {
    override val inflate: Inflate<FragmentCreateUserBinding>
        get() = FragmentCreateUserBinding::inflate

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getInjectViewModel(): MainViewModel = mainViewModel

    override fun initialization() {
        binding.spGender.setItems(
            Gender.entries.map {
                SpinnerItemsAdapter.SpinnerItem(id = it.value, text = getString(it.labelRes))
            }.toList()
        )
    }

    override fun listeners() {
        binding.apply {
            btnSave.setOnClickListener {
                User.CreateUserRequest(name = etName.getComponentText(),
                    age = ncAge.getSelectedNumber(),
                    jobTitle = etJob.getComponentText(),
                    gender = Gender.entries.find { it.value == spGender.getSelectedItem()?.id })
                    .validate(object : User.CreateUserRequest.Listener {
                        override fun success(user: User) {
                            mainViewModel.insertUser(user)
                        }

                        override fun error(type: EditTextType, errorRes: Int?) {
                            viewInputError(
                                type,
                                if (errorRes != null) {
                                    getString(errorRes)
                                } else {
                                    getString(R.string.error_something_went_wrong)
                                },
                            )
                        }
                    })
            }

            btnViewData.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(
                    CreateUserFragmentDirections.actionCreateUserFragmentToViewDataFragment(),
                )
            }
        }
    }

    private fun viewInputError(type: EditTextType, error: String) {
        when (type) {
            EditTextType.NAME -> {
                binding.etName.setError(error)
            }

            EditTextType.AGE -> {
                binding.ncAge.setError(error)
            }

            EditTextType.JOB_TITLE -> {
                binding.etJob.setError(error)
            }

            EditTextType.GENDER -> {
                binding.spGender.setError(error)
            }

            // Here set the exist values in this fragment only.
            // But set "else" state if there is more values which not in this page.
        }
    }

    override fun initializeViewModel() {
        initializeCreateUserObserver()
    }

    private fun initializeCreateUserObserver() {
        mainViewModel.insertUserLiveData.observe(viewLifecycleOwner) {
            when (it) {
                BaseUiResource.LoadingState -> {}

                is BaseUiResource.FailureState -> {
                    (if (it.messageRes != null) getString(it.messageRes)
                    else getString(R.string.error_something_went_wrong)).toast()
                }

                is BaseUiResource.SuccessState -> {
                    getString(R.string.user_inserted_successfully).toast()
                    resetViews()
                }
            }
        }
    }

    private fun resetViews() {
        binding.apply {
            etName.resetComponent()
            ncAge.resetComponent()
            etJob.resetComponent()
            spGender.resetComponentSelection()
        }
    }
}