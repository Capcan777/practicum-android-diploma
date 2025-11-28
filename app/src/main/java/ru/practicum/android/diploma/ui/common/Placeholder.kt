package ru.practicum.android.diploma.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

@Composable
fun Placeholder(
    imageResId: Int,
    title: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .width(328.dp)
                .height(223.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = VacancyTheme.typography.medium22,
            text = title,
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun PlaceholderPreview() {
    VacancyTheme(isDarkTheme = false) {
        Placeholder(imageResId = R.drawable.placeholder_error_riecive, title = "No connection")
    }
}
