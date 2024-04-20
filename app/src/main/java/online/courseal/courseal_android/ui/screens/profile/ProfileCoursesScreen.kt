package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.viewmodels.ProfileScreenViewModel

@Composable
fun ProfileCoursesScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onOpenCourse: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    profileScreenViewModel: ProfileScreenViewModel
) {
    val profileScreenUiState by profileScreenViewModel.uiState.collectAsState()

    Column {
        profileScreenUiState.userPublicInfo!!.courses.forEach { course ->
            Text(text = "${course.courseName} ${course.xp} XP")
        }
    }
}