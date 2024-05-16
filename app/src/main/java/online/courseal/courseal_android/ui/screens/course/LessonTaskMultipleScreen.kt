package online.courseal.courseal_android.ui.screens.course

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskMultipleExamAnswer
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskMultiple
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskMultiple
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealSelectableButton
import online.courseal.courseal_android.ui.components.editorjs.content.EditorJSContentComponent
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartViewModel
import online.courseal.courseal_android.ui.viewmodels.course.TaskAnswer

sealed class TaskMultipleAnswer {
    class PracticeTrainingTask(val task: CoursealTaskMultiple) : TaskMultipleAnswer()
    class ExamTask(val task: CoursealExamTaskMultiple) : TaskMultipleAnswer()
}

@Composable
fun LessonTaskMultipleScreen(
    modifier: Modifier = Modifier,
    content: TaskMultipleAnswer,
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
                is TaskMultipleAnswer.ExamTask -> content.task.body
                is TaskMultipleAnswer.PracticeTrainingTask -> content.task.body
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
            is TaskMultipleAnswer.PracticeTrainingTask -> content.task.options.map { it.text }
            is TaskMultipleAnswer.ExamTask -> content.task.options.map { it.text }
        }

        val selectedOptions = rememberSaveable(content) { mutableListOf<Int>() }

        options.forEachIndexed { index, option ->
            CoursealSelectableButton(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.85f),
                text = option,
                selected = index in selectedOptions,
                onClick = {
                    if (index in selectedOptions) {
                        selectedOptions.remove(index)
                    } else {
                        selectedOptions.add(index)
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
            onClick = {
                val answer = when (content) {
                    is TaskMultipleAnswer.PracticeTrainingTask -> {
                        val correctOptions = content.task.options
                            .mapIndexed { index, option -> Pair(index, option) }
                            .filter { it.second.isCorrect }
                            .map { it.first }

                        TaskAnswer.PracticeTrainingAnswer(selectedOptions.toSet() == correctOptions.toSet())
                    }
                    is TaskMultipleAnswer.ExamTask -> TaskAnswer.ExamAnswer(TaskMultipleExamAnswer(selectedOptions))
                }
                lessonStartViewModel.saveAnswer(answer)
            }
        )
    }
}