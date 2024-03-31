package online.courseal.courseal_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CoursealTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = true
) {
    val labelContent: @Composable (() -> Unit)? = if (label != null) {
        { Text(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface),
            text = label
        ) }
    } else null

    val placeholderContent: @Composable (() -> Unit)? = if (placeholder != null) {
        { Text(
            text = placeholder
        ) }
    } else null

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = labelContent,
        placeholder = placeholderContent,
        singleLine = singleLine,
        shape = RoundedCornerShape(size = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}