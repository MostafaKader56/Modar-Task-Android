package com.modar.task.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding, MV : BaseViewModel> : Fragment() {

    protected abstract val inflate: Inflate<VB>
    private var _binding: VB? = null
    val binding get() = _binding!!

    private val viewModel: MV by lazy {
        getInjectViewModel()
    }

    abstract fun getInjectViewModel(): MV

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    abstract fun initialization()

    abstract fun listeners()

    abstract fun initializeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewModel()
        initialization()
        listeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}