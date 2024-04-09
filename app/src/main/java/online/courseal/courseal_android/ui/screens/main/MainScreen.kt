package online.courseal.courseal_android.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onViewAccounts: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /*TODO*/ },
                    icon = { Text("Course") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Text("Profile") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Text("Creator") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            Text("hmm")
        }
        CoursealPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "view accounts",
            onClick = {
            onViewAccounts()
        })
    }
}