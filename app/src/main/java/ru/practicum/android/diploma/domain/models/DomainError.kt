package ru.practicum.android.diploma.domain.models

sealed interface DomainError {
    object NoConnection : DomainError
    object OtherError : DomainError
}
