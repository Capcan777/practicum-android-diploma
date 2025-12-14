package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vacancyId: String,
    val title: String,
    val description: String,
    val salary: String,
    val experience: String,
    val company: String,
    val location: String,
    val industry: String,
    val employment: String,
    val schedule: String,
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String,
    val skills: String
)
