package com.modar.task.base

open class BaseRoomResource<out T>(
    open val response: T? = null,
    open val message: String? = null,
)