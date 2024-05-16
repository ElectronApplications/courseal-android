package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.TopBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.profile.ProfileViewModel

@Composable
fun ProfileCoursesScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onOpenCourse: (courseId: Int) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    profileViewModel: ProfileViewModel
) {
    val profileUiState by profileViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TopBack(onClick = onGoBack)

        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally)
                .adaptiveContainerWidth()
        ) {
            val userPublicInfo = profileUiState.userPublicInfo!!

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
                    .padding(top = 8.dp)
            ) {
                userPublicInfo.courses.forEach { course ->
                    CoursealOutlinedCardItem(
                        modifier = Modifier.clickable { onOpenCourse(course.courseId) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f, fill = false),
                            text = course.courseName,
                            fontWeight = FontWeight.SemiBold
                        )
                        Image(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .width(20.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = course.courseName,
                            contentScale = ContentScale.FillWidth,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            }
        }
    }
}