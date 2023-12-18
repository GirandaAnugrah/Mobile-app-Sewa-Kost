package com.example.uas_koskosan_kelompok5.view.component

import android.os.Build
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

    val context = LocalContext.current
    val onBackPressedDispatcher = (context as? OnBackPressedDispatcherOwner)?.onBackPressedDispatcher
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
//      SelectableWeekCalendar()
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Back",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onBackPressedDispatcher?.onBackPressed()
                    }
                )
            }
        }

        Text(
            text = "Select a date to start renting",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )

        SelectableCalendar(
            calendarState = calendarState,
        )

        Text(
            text = "Selected date: "
                    + calendarState.selectionState.selection.toString(),
            modifier = Modifier.padding(top = 10.dp)
        )
//        Text(text = transactionDay)
        Button(onClick = {
            addDay(calendarState.selectionState.selection[0])
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
            ) {
            Text(text = "Send")
        }
    }

}

