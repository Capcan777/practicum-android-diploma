package ru.practicum.android.diploma.data.dto

data class ContactsDto(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: List<String>? = null
)
