package ru.practicum.android.diploma.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.ExternalNavigator
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
        application.startActivity(intent)
    }

    override fun callPhone(telephoneNumber: String?) {
        if (telephoneNumber.isNullOrBlank()) {
            return
        }
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telephoneNumber"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    fun getTextToShare(vacancy: Vacancy): String = vacancy.url ?: ""
}
