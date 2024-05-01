package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.components.CoursealDropdownMenu
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.editor.EditorUiError
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

enum class PagerItems(val titleId: Int) {
    STRUCTURE(R.string.structure),
    LESSONS(R.string.lessons),
    TASKS(R.string.tasks)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    onCreateCourse: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val editorUiState by editorViewModel.uiState.collectAsState()

    if (editorUiState.errorUnrecoverableState != null) {
        onUnrecoverable(editorUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = editorUiState.errorState != EditorUiError.NONE,
        hideDialog = editorViewModel::hideError,
        title = when (editorUiState.errorState) {
            EditorUiError.COURSE_NOT_FOUND -> stringResource(R.string.course_not_found)
            EditorUiError.NO_PERMISSIONS -> stringResource(R.string.no_permissions)
            EditorUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            EditorUiError.NONE -> ""
        }
    )

    LaunchedEffect(key1 = editorUiState.loading) {
        if (editorUiState.needUpdate) {
            editorViewModel.update()
        }
    }

    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        CoursealTopBar(
            dividerEnabled = !dropdownExpanded && (editorUiState.loading || editorUiState.courseInfo == null)
        ) {
            Row(
                modifier = Modifier.clickable { dropdownExpanded = !dropdownExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.your_courses),
                    style = MaterialTheme.typography.headlineSmall
                )
                AnimatedArrowDown(isExpanded = dropdownExpanded)
            }
        }

        Box {
            if (editorUiState.loading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else if (editorUiState.courseInfo != null) {
                Column {
                    val pagerState = rememberPagerState(pageCount = { PagerItems.entries.size })
                    
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        PagerItems.entries.forEachIndexed { index, entry ->
                            Tab(
                                selected = index == pagerState.currentPage,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 12.dp),
                                    text = stringResource(entry.titleId)
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState
                    ) { page ->
                        when (page) {
                            PagerItems.STRUCTURE.ordinal -> {
                                EditorStructureTab(
                                    modifier = Modifier.fillMaxSize(),
                                    onUnrecoverable = onUnrecoverable,
                                    editorViewModel = editorViewModel
                                )
                            }
                            PagerItems.LESSONS.ordinal -> {
                                EditorLessonsTab(
                                    modifier = Modifier.fillMaxSize(),
                                    onUnrecoverable = onUnrecoverable,
                                    editorViewModel = editorViewModel
                                )
                            }
                            PagerItems.TASKS.ordinal -> {
                                EditorTasksTab(
                                    modifier = Modifier.fillMaxSize(),
                                    onUnrecoverable = onUnrecoverable,
                                    editorViewModel = editorViewModel
                                )
                            }
                        }
                    }
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

            this@Column.CoursealDropdownMenu(
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
                    editorUiState.courses?.forEach { course ->
                        CoursealOutlinedCardItem(
                            modifier = Modifier.clickable {
                                dropdownExpanded = false
                                coroutineScope.launch {
                                    editorViewModel.switchCourse(course.courseId)
                                }
                            },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
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

                CoursealPrimaryButton(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.85f),
                    text = stringResource(R.string.create_course),
                    enabled = editorUiState.canCreateCourses,
                    onClick = onCreateCourse
                )
            }
        }
    }
}