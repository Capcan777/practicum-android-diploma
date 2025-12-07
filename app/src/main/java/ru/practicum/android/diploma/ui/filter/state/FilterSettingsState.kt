package ru.practicum.android.diploma.ui.filter.state

data class FilterSettingsState(
    val salary: String = "",
    val industry: String = "",
    val placeOfWork: String = "",
    val hideWithoutSalary: Boolean = false
) {
    val isFilterApplied: Boolean
        get() = salary.isNotEmpty() || industry.isNotEmpty() || placeOfWork.isNotEmpty() || hideWithoutSalary
}
