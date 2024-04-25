package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.AnimatedArrowDown
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.components.CoursealDropdownMenu
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.viewmodels.editor.EditorUiError
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel = hiltViewModel()
) {
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

    LaunchedEffect(key1 = true) {
        if (editorUiState.needUpdate) {
            editorViewModel.update()
        }
    }

    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        CoursealTopBar {
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
            }

            this@Column.CoursealDropdownMenu(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                visible = dropdownExpanded,
                setVisible = { dropdownExpanded = it }
            ) {

            }
        }
    }
}