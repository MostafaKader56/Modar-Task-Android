package com.modar.task.ui.main

import androidx.fragment.app.activityViewModels
import com.modar.task.base.BaseFragment
import com.modar.task.base.Inflate
import com.modar.task.databinding.FragmentCreateUserBinding
import com.modar.task.viewmodels.main.MainViewModel

class CreateUserFragment : BaseFragment<FragmentCreateUserBinding, MainViewModel>() {
    override val inflate: Inflate<FragmentCreateUserBinding>
        get() = FragmentCreateUserBinding::inflate

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getInjectViewModel(): MainViewModel = mainViewModel

    override fun initialization() {

    }

    override fun listeners() {

    }

    override fun initializeViewModel() {

    }
}