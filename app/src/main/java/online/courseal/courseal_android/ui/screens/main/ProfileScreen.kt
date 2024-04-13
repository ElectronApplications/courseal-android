package online.courseal.courseal_android.ui.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onViewAccounts: () -> Unit,
    onUnrecoverable: OnUnrecoverable
) {
    CoursealPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "view accounts",
        onClick = {
            onViewAccounts()
        }
    )
}