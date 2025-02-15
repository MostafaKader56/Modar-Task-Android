package com.modar.task.ui.main

import android.view.LayoutInflater
import com.modar.task.base.BaseActivity
import com.modar.task.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingFactory: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun initialization() {}

    override fun setListeners() {}
}