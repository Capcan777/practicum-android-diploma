package ru.practicum.android.diploma.domain.models

data class Contacts(
    val name: String? = null,
    val email: String? = null,
    val phones: List<Phone>? = null
)
