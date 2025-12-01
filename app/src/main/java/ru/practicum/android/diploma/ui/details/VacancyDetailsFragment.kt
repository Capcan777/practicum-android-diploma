package ru.practicum.android.diploma.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

class VacancyDetailsFragment : Fragment() {
    private var vacancyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vacancyId = it.getString("vacancyId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                VacancyTheme(isDarkTheme = false) {
                    val navController = findNavController()
                    VacancyDetailsScreen(
                        vacancyId = vacancyId ?: "",
                        navController = navController,
                        onBack = { navController.popBackStack() },
                        onShare = { },
                        onToggleFavorite = { }
                    )
                }
            }
        }
    }
}
