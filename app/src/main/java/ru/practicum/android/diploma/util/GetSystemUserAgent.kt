package ru.practicum.android.diploma.util

import android.content.Context
import android.webkit.WebSettings

object GetSystemUserAgent {

    fun systemUserAgent(context: Context): String {
        return WebSettings.getDefaultUserAgent(context)
    }
}
