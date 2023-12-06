package com.example.uas_koskosan_kelompok5.view.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.SelectableWeekCalendar
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.StaticWeekCalendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(addDay:(day: LocalDate) -> Unit) {
//    StaticCalendar(
//        dayContent = { dayState ->  addDay(dayState.date.toString())}
//    )
//    var transactionDay by remember { mutableStateOf("") }
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
//      SelectableWeekCalendar()
        Text(text = "Select a date to start renting")
        SelectableCalendar(
            calendarState = calendarState,
        )

        Text(text = calendarState.selectionState.selection.toString())
//        Text(text = transactionDay)
        Button(onClick = {
            addDay(calendarState.selectionState.selection[0])
        }) {
            Text(text = "Send")
        }
    }

}

