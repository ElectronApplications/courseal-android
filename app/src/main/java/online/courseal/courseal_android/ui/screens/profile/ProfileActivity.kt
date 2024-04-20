package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.api.user.UserActivityApiResponse
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun ProfileActivity(
    modifier: Modifier = Modifier,
    userActivity: UserActivityApiResponse
) {
    val activity = userActivity.activity.associate { Pair(it.day.toEpochDays(), it.xp) }

    val palette = LocalCoursealPalette.current.activityPalette

    val currentDay = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val currentDayOfWeek = currentDay.dayOfWeek.isoDayNumber

    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.monday_short), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.thursday_short), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.sunday_short), style = MaterialTheme.typography.bodyMedium)
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val gapSize = 5.0f
            val length = (size.height - 6 * gapSize) / 7
            val amountWidth = (size.width / (length + gapSize)).toInt()
            val leftOffset = (size.width - amountWidth * (length + gapSize)) / 2

            val daysAmount = 7 * (amountWidth - 1) + currentDayOfWeek

            var day = currentDay.toEpochDays() - daysAmount + 1
            repeat(amountWidth) { x ->
                repeat(7) { y ->
                    val color = when (activity[day] ?: 0) {
                        0 -> palette[0]
                        in 1..10 -> palette[1]
                        in 11..30 -> palette[2]
                        else -> palette[3]
                    }
                    if (!(x == amountWidth - 1 && y >= currentDayOfWeek)) {
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(leftOffset + x * (length + gapSize), y * (length + gapSize)),
                            size = Size(length, length),
                            cornerRadius = CornerRadius(7.5f)
                        )
                    }

                    day += 1
                }
            }
        }
    }

}