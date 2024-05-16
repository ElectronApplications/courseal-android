package online.courseal.courseal_android.ui.screens.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.AnimatedArrowDown
import online.courseal.courseal_android.ui.components.CoursealDropdownScreen
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.course.CourseUiError
import online.courseal.courseal_android.ui.viewmodels.course.CourseViewModel

@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    onSearchCourses: () -> Unit,
    onViewCourse: (courseId: Int) -> Unit,
    onStartLesson: (lessonId: Int) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    courseViewModel: CourseViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val courseUiState by courseViewModel.uiState.collectAsState()

    if (courseUiState.errorUnrecoverableState != null) {
        onUnrecoverable(courseUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = courseUiState.errorState != CourseUiError.NONE,
        hideDialog = courseViewModel::hideError,
        title = when (courseUiState.errorState) {
            CourseUiError.COURSE_NOT_FOUND -> stringResource(R.string.course_not_found)
            CourseUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            CourseUiError.NONE -> ""
        }
    )

    LaunchedEffect(key1 = courseUiState.needUpdate) {
        if (courseUiState.needUpdate) {
            courseViewModel.update()
        }
    }

    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CoursealTopBar(
            dividerEnabled = !dropdownExpanded
        ) {
            Row(
                modifier = Modifier.clickable { dropdownExpanded = !dropdownExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.view_courses),
                    style = MaterialTheme.typography.headlineSmall
                )
                AnimatedArrowDown(isExpanded = dropdownExpanded)
            }
        }

        Box {
            if (courseUiState.loading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else if (courseUiState.courseInfo != null) {
                Column {
                    CourseStructureTab(
                        modifier = Modifier.fillMaxSize(),
                        onStartLesson = onStartLesson,
                        onUnrecoverable = onUnrecoverable,
                        courseViewModel = courseViewModel
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_course_chosen)
                    )
                }
            }

            this@Column.CoursealDropdownScreen(
                modifier = Modifier
                    .adaptiveContainerWidth()
                    .verticalScroll(rememberScrollState()),
                visible = dropdownExpanded,
                setVisible = { dropdownExpanded = it }
            ) {
                CoursealOutlinedCard(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.85f)
                ) {
                    courseUiState.coursesList?.forEach { course ->
                        CoursealOutlinedCardItem(
                            modifier = Modifier.clickable {
                                dropdownExpanded = false
                                coroutineScope.launch {
                                    courseViewModel.switchCourse(course.courseId)
                                }
                            },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f, fill = false),
                                text = course.courseName,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row {
                                Image(
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .width(20.dp)
                                        .clickable { onViewCourse(course.courseId) },
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = stringResource(R.string.view_course),
                                    contentScale = ContentScale.FillWidth,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
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

                CoursealPrimaryButton(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.85f),
                    text = stringResource(R.string.find_courses),
                    onClick = onSearchCourses
                )
            }
        }
    }
}