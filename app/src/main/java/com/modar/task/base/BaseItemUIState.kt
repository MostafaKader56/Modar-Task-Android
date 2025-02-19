package com.modar.task.base

open class BaseItemUIState<out T : Any>(
    open val item: T?,
    open val position: Int,
    open var success: Boolean = false,
    open var errorMessageRes: Int? = null
)

