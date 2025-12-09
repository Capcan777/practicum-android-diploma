package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class FilterIndustryDto(
    val id: Int,
    val name: String,
    @SerializedName("industries")
    val industries: List<FilterIndustryDto>? = null
)
