package com.modar.task.base

open class BaseItemUIState<out T : Any>(
    open val item: T?,
    open var success: Boolean = false,
    open var errorMessage: String? = null
)

