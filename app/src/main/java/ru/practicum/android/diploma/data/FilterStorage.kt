package ru.practicum.android.diploma.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class FilterStorage(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveFilterParameters(parameters: FilterParameters) {
        val json = gson.toJson(parameters)
        sharedPreferences.edit {
            putString(KEY_FILTER_PARAMETERS, json)
        }
    }

    fun getFilterParameters(): FilterParameters {
        val json = sharedPreferences.getString(KEY_FILTER_PARAMETERS, null)
        return if (json != null) {
            try {
                gson.fromJson(json, FilterParameters::class.java)
            } catch (e: JsonSyntaxException) {
                Log.e(JSON_PARSING, "Не удалось разобрать FilterParameters из JSON", e)
                FilterParameters()
            }
        } else {
            FilterParameters()
        }
    }

    fun clearFilterParameters() {
        sharedPreferences.edit {
            remove(KEY_FILTER_PARAMETERS)
        }
    }

    companion object {
        private const val PREFS_NAME = "filter_preferences"
        private const val KEY_FILTER_PARAMETERS = "filter_parameters"
        private const val JSON_PARSING = "JSON_PARSING"
    }
}

