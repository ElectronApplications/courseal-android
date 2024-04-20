package online.courseal.courseal_android.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CoursealOutlinedCard(
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.outlinedCardElevation(
            defaultElevation = 1.dp
        ),
        content = content
    )
}