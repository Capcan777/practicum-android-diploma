package ru.practicum.android.diploma.data

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.ExternalNavigator
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ResourceProvider

private const val DESCRIPTION_PREVIEW_LENGTH = 200

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

    override fun sendEmail(vacancy: Vacancy) {
        val emailAddress = vacancy.contacts?.email
        if (emailAddress.isNullOrBlank()) {
            return
        }

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, resourceProvider.getString(R.string.apply_for_vacancy, vacancy.title))
        }

        try {
            application.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                application,
                resourceProvider.getString(R.string.no_email_clients_found),
                Toast.LENGTH_SHORT
            ).show()
        }
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
        strBuilder.append(vacancy.description.take(DESCRIPTION_PREVIEW_LENGTH))
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
                } else {
                    "$salary.from — $salary.to$curr"
                }

            salary.from != null -> "от $salary.from$curr"
            salary.to != null -> "до $salary.to$curr"
            else -> resourceProvider.getString(R.string.salary_not_specified)
        }
    }
}
