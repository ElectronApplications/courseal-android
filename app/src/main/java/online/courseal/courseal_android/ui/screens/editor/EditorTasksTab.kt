package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

@Composable
fun EditorTasksTab(
    modifier: Modifier = Modifier,
    onCreateTask: () -> Unit,
    onEditTask: (taskId: Int) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel
) {
    val editorUiState by editorViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val tasks = editorUiState.courseTasks

        if (editorUiState.courseInfo != null) {
            CoursealPrimaryButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.85f),
                text = stringResource(R.string.create_task),
                onClick = onCreateTask
            )
        }

        if (tasks != null) {
            CoursealOutlinedCard(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.85f)
            ) {
                LazyColumn {
                    items(tasks) {  task ->
                        CoursealOutlinedCardItem(
                            modifier = Modifier.clickable { onEditTask(task.taskId) },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f, fill = false),
                                text = task.taskName,
                                fontWeight = FontWeight.SemiBold
                            )
                            Image(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .width(20.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(R.string.view_task),
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