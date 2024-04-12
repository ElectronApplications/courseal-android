package online.courseal.courseal_android.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CoursealPrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = 10.dp),
        contentPadding = PaddingValues(all = 13.dp),
        elevation = ButtonDefaults.elevatedButtonElevation()
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun CoursealSecondaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = 10.dp),
        contentPadding = PaddingValues(all = 13.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(
            text = text
        )
    }
}