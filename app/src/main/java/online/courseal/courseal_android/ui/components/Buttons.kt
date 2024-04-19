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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

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
fun CoursealPrimaryLoadingButton(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    text: String,
    onClick: () -> Unit
) {
    CoursealPrimaryButton(
        modifier = modifier,
        enabled = !loading,
        text = if (!loading) text else stringResource(R.string.loading),
        onClick = onClick
    )
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

@Composable
fun CoursealSelectableButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    setSelected: (Boolean) -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = { setSelected(!selected) },
        shape = RoundedCornerShape(size = 10.dp),
        contentPadding = PaddingValues(all = 13.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
            contentColor = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun CoursealSuccessButton(
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
            containerColor = LocalCoursealPalette.current.success,
            contentColor = LocalCoursealPalette.current.onSuccess
        )
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun CoursealErrorButton(
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
            containerColor = LocalCoursealPalette.current.error,
            contentColor = LocalCoursealPalette.current.onError
        )
    ) {
        Text(
            text = text
        )
    }
}