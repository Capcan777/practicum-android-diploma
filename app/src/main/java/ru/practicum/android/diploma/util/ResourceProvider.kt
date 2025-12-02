package ru.practicum.android.diploma.util

import android.content.Context

class ResourceProvider(private val context: Context) {
    fun getString(resId: Int, vararg args: Any?): String =
        context.getString(resId, *args)
}
