package dialogs

import ClientManager
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.NumberPicker
import kotlinx.coroutines.launch
import models.Employee
import screens.tryParseDouble
import java.time.LocalDateTime
import java.time.Month

@Composable
fun CreatingTaskDialog(
    value : MutableState<Boolean>,
    employeeId: Int,
    refreshLambda: suspend ()-> Unit
) {
    val endingDate = LocalDateTime.now()
    AlertDialog(
        modifier = Modifier.fillMaxSize(0.9f),
        onDismissRequest = { value.value = !value.value },
        buttons = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).horizontalScroll(rememberScrollState()).padding(10.dp)
            ) {
                val scope = rememberCoroutineScope()
                var name by remember {
                    mutableStateOf("")
                }
                var description by remember {
                    mutableStateOf("")
                }
                var amount by remember {
                    mutableDoubleStateOf(0.0)
                }

                val years = (endingDate.year..endingDate.year+100)
                val selectedYear = remember {
                    mutableIntStateOf(years.first)
                }

                val months = (1..12)
                val selectedMonth = remember {
                    mutableIntStateOf(endingDate.month.value)
                }

                var days: IntRange
                val selectedDay = remember {
                    mutableIntStateOf(endingDate.dayOfMonth)
                }
                val
                        hours = (0..23)
                val selectedHour = remember {
                    mutableIntStateOf(endingDate.hour)
                }

                val minutes = (0..59)
                val selectedMinute = remember {
                    mutableIntStateOf(endingDate.minute)
                }

                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {Text("Название")},
                    modifier = Modifier.width(200.dp)
                )
                TextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {Text("Описание")},
                    modifier = Modifier.width(230.dp)
                )
                TextField(
                    value = amount.toString(),
                    label = {Text("Награда")},

                    onValueChange = {
                        if (it.tryParseDouble()){
                            amount = it.toDouble()
                        }
                    },
                    modifier = Modifier.width(100.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    NumberPicker(years, selectedYear, "Г")
                    NumberPicker(months, selectedMonth, "М")
                    key(selectedMonth.intValue, selectedYear.intValue){
                         days = (selectedMonth.intValue.getMonthDays(selectedYear.intValue))
                         selectedDay.value = days.first
                        NumberPicker(days, selectedDay, "Д")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    NumberPicker(
                        hours,
                        selectedHour,
                        "Ч"
                    )
                    NumberPicker(
                        minutes,
                        selectedMinute,
                        "Мин"
                    )
                }
                Button(
                    onClick = {
                        LocalDateTime.of(
                            selectedYear.intValue,
                            selectedMonth.intValue,
                            selectedDay.intValue,
                            selectedHour.intValue,
                            selectedMinute.intValue,
                        ).let {localDateTime->
                            scope.launch {
                                ClientManager.createTask(
                                    name = name,
                                    description = description,
                                    amount = amount,
                                    creatingDate = LocalDateTime.now(),
                                    employeeId = employeeId,
                                    endingDate = localDateTime
                                )
                                refreshLambda()
                            }
                        }
                    }
                ){
                    Text("Сохранить")
                }
            }
        }
    )
}

fun Int.getMonthDays(year: Int): IntRange = when(this){
    1, 3, 5, 7, 8, 10, 12 -> {
        IntRange(1, 31)
    }
    2 -> if (year % 4 == 0) IntRange(1, 29) else IntRange(1, 28)
        else ->  IntRange(1, 30)
}