package ru.practicum.android.diploma.data

data class FilterParameters(
    val salary: String = "",
    val industry: String = "",
    val placeOfWork: String = "",
    val hideWithoutSalary: Boolean = false
)
