package ru.practicum.android.diploma.data

import android.app.Application
import android.content.Intent
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.ExternalNavigator
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ResourceProvider

class ExternalNavigatorImpl(
    private val application: Application,
    private val resourceProvider: ResourceProvider
) : ExternalNavigator {
    override fun shareVacancy(vacancy: Vacancy) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val vacancyToShare = getTextToShare(vacancy)

        intent.putExtra(Intent.EXTRA_TEXT, vacancyToShare)
        val shareIntent = Intent.createChooser(intent, vacancy.title)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)
    }

    fun getTextToShare(vacancy: Vacancy): String {
        val strBuilder = StringBuilder()
        strBuilder.append(vacancy.title)
        vacancy.company.name.let {
            strBuilder.append(" — ").append(it)
        }
        vacancy.location?.let {
            strBuilder.append("\n").append(it)
        }
        vacancy.salary?.let {
            strBuilder.append("\n")
                .append(
                    resourceProvider.getString(R.string.salary_for_sharing)
                )
                .append(salaryRangeToStr(it))
        }
        strBuilder.append("\n\n")
        strBuilder.append(vacancy.description.take(200))
        strBuilder.append("...")
        vacancy.url?.let { strBuilder.append("\n\n").append(it) }
        return strBuilder.toString()
    }

    private fun salaryRangeToStr(salary: SalaryRange): String {
        val curr = salary.currency?.let { " $it" } ?: ""
        return when {
            salary.from != null && salary.to != null ->
                if (salary.from == salary.to) {
                    "$salary.from$curr"
                } else "$salary.from — $salary.to$curr"

            salary.from != null -> "от $salary.from$curr"
            salary.to != null -> "до $salary.to$curr"
            else -> resourceProvider.getString(R.string.salary_not_specified)
        }
    }
}
