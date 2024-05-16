package online.courseal.courseal_android.ui.screens.course

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskSingleExamAnswer
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskSingle
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskSingle
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealSelectableButton
import online.courseal.courseal_android.ui.components.editorjs.content.EditorJSContentComponent
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartViewModel
import online.courseal.courseal_android.ui.viewmodels.course.TaskAnswer

sealed class TaskSingleAnswer {
    class PracticeTrainingTask(val task: CoursealTaskSingle) : TaskSingleAnswer()
    class ExamTask(val task: CoursealExamTaskSingle) : TaskSingleAnswer()
}

@Composable
fun LessonTaskSingleScreen(
    modifier: Modifier = Modifier,
    content: TaskSingleAnswer,
    lessonStartViewModel: LessonStartViewModel
) {
    Column(
        modifier = modifier
    ) {
        EditorJSContentComponent(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.85f),
            content = when (content) {
                is TaskSingleAnswer.ExamTask -> content.task.body
                is TaskSingleAnswer.PracticeTrainingTask -> content.task.body
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.85f),
            fontWeight = FontWeight.SemiBold,
            text = stringResource(R.string.choose_correct_answer)
        )

        val options = when(content) {
            is TaskSingleAnswer.PracticeTrainingTask -> content.task.options
            is TaskSingleAnswer.ExamTask -> content.task.options
        }

        var currentOption by rememberSaveable(content) { mutableStateOf<Int?>(null) }

        options.forEachIndexed { index, option ->
            CoursealSelectableButton(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.85f),
                text = option,
                selected = index == currentOption,
                onClick = {
                    currentOption = if (currentOption == index) {
                        null
                    } else {
                        index
                    }
                }
            )
        }

        CoursealPrimaryButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.85f)
                .padding(top = 12.dp),
            text = stringResource(R.string.confirm),
            enabled = currentOption != null,
            onClick = {
                val answer = when (content) {
                    is TaskSingleAnswer.PracticeTrainingTask -> TaskAnswer.PracticeTrainingAnswer(currentOption == content.task.correctOption)
                    is TaskSingleAnswer.ExamTask -> TaskAnswer.ExamAnswer(TaskSingleExamAnswer(currentOption!!))
                }
                lessonStartViewModel.saveAnswer(answer)
            }
        )
    }
}