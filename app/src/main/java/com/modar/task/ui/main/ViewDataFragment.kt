package com.modar.task.ui.main

import androidx.fragment.app.activityViewModels
import com.modar.task.base.BaseFragment
import com.modar.task.base.Inflate
import com.modar.task.databinding.FragmentViewDataBinding
import com.modar.task.viewmodels.main.MainViewModel

class ViewDataFragment : BaseFragment<FragmentViewDataBinding, MainViewModel>() {
    override val inflate: Inflate<FragmentViewDataBinding>
        get() = FragmentViewDataBinding::inflate

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getInjectViewModel(): MainViewModel = mainViewModel

    override fun initialization() {

    }

    override fun listeners() {

    }

    override fun initializeViewModel() {

    }
}