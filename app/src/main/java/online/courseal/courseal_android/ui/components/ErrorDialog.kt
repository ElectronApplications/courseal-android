package online.courseal.courseal_android.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import online.courseal.courseal_android.R

@Composable
fun ErrorDialog(
    isVisible: Boolean,
    setVisible: (value: Boolean) -> Unit,
    title: String,
    text: String? = null
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = { setVisible(false) },
            confirmButton = {
                TextButton(onClick = { setVisible(false) }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            title = { Text(title) },
            text = if (text != null) {{ Text(text) }} else null
        )
    }
}