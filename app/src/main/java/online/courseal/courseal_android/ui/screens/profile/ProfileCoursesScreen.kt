package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.GoBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileCoursesScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onOpenCourse: (courseId: Int) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    profileViewModel: ProfileViewModel
) {
    val profileScreenUiState by profileViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        GoBack(onGoBack = onGoBack)

        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally)
                .adaptiveContainerWidth()
        ) {
            val userPublicInfo = profileScreenUiState.userPublicInfo!!

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.user_courses, userPublicInfo.username),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            CoursealOutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
            ) {
                userPublicInfo.courses.forEach { course ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenCourse(course.courseId) }
                    ) {
                        Text(course.courseName)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}