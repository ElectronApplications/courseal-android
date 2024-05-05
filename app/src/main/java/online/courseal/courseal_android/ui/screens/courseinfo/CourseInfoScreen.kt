package online.courseal.courseal_android.ui.screens.courseinfo

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.courseinfo.CourseInfoUiError
import online.courseal.courseal_android.ui.viewmodels.courseinfo.CourseInfoViewModel

@Composable
fun CourseInfoScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onOpenProfile: (usertag: String) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    courseInfoViewModel: CourseInfoViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val courseInfoUiState by courseInfoViewModel.uiState.collectAsState()

    if (courseInfoUiState.errorUnrecoverableState != null) {
        onUnrecoverable(courseInfoUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = courseInfoUiState.errorState != CourseInfoUiError.NONE,
        hideDialog = courseInfoViewModel::hideError,
        title = when (courseInfoUiState.errorState) {
            CourseInfoUiError.COURSE_NOT_FOUND -> stringResource(R.string.course_not_found)
            CourseInfoUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            CourseInfoUiError.NONE -> ""
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TopBack(onClick = onGoBack)

        if (courseInfoUiState.loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            val courseInfo = courseInfoUiState.courseInfo
            if (courseInfo != null) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .adaptiveContainerWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .align(Alignment.CenterHorizontally),
                        text = courseInfo.courseName,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp),
                        text = courseInfo.courseDescription,
                    )

                    CoursealPrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp),
                        text = if (courseInfoUiState.isEnrolled != true)
                            stringResource(R.string.enroll) else stringResource(R.string.already_enrolled),
                        enabled = courseInfoUiState.isEnrolled != true && !courseInfoUiState.enrolling,
                        onClick = {
                            coroutineScope.launch {
                                courseInfoViewModel.enroll()
                            }
                        }
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 24.dp),
                        text = stringResource(R.string.course_maintainers),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )

                    CoursealOutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    ) {
                        courseInfo.courseMaintainers.forEach { maintainer ->
                            CoursealOutlinedCardItem(
                                modifier = Modifier.clickable { onOpenProfile(maintainer.usertag) },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "@${maintainer.usertag}"
                                )
                                Image(
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .width(20.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = stringResource(R.string.profile),
                                    contentScale = ContentScale.FillWidth,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}