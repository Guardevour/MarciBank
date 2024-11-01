package screens

import ClientManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import components.BottomNavBar
import dialogs.CreatingTaskDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Completing
import models.Employee
import models.Task
import screens.employeescreens.AdminScreen
import screens.employeescreens.DirectorScreen
import screens.employeescreens.EmployeeAndManagerScreen
import java.time.LocalDateTime

@Composable
fun TasksScreen(
    navHostController: NavHostController,
    employeeJson: String
) {
    val employee = Json.decodeFromString<Employee>(employeeJson)
    val availableTasks: SnapshotStateList<Pair<String, Task>> = SnapshotStateList()
    val scope = rememberCoroutineScope()
    var selectedIndex by remember {
        mutableIntStateOf(-1)
    }
    val isFormVisible by derivedStateOf {
        employee.accessLevel == Titles.Manager.text && selectedIndex != -1
    }
    val isCreatingDialogOpen = remember {
        mutableStateOf(false)
    }
    val bottomButtons = mutableMapOf<ImageVector, () -> Unit>()
    bottomButtons.putAll(
        mapOf(
            Icons.Filled.Person to {
                navHostController.navigate("employee/${employeeJson}")
            },
            Icons.Filled.Menu to {
                navHostController.navigate("tasks/${employeeJson}")
            },
            Icons.Filled.Warning to {
                navHostController.navigate("penaltings/${employeeJson}")
            },
            Icons.AutoMirrored.Filled.ExitToApp to {
                navHostController.navigate("auth")
            }
        )
    )

    if (employee.accessLevel == Titles.Manager.text){
        bottomButtons[Icons.Filled.CheckCircle] = {
            navHostController.navigate("audition/${employeeJson}")
        }
        bottomButtons[Icons.Filled.Notifications] = {
            navHostController.navigate("penalty/${employeeJson}")
        }
    }

    SideEffect {
        scope.launch {
            availableTasks.clear()
            ClientManager.getAllDepartmentTask(employee.departmentId).forEach{
                availableTasks.add(it.key to it.value)
            }
        }
    }

    if (isCreatingDialogOpen.value){
        CreatingTaskDialog(
            value = isCreatingDialogOpen,
            employeeId = employee.id,
            suspend {
                availableTasks.clear()
                ClientManager.getAllDepartmentTask(employee.departmentId).forEach{
                    availableTasks.add(it.key to it.value)
                }
                isCreatingDialogOpen.value = false
            }
        )
    }

    Screen {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            items(availableTasks.size) { index ->
                availableTasks[index].second.draw(
                    additionalOnClick = {selectedIndex = index},
                    createCompleting = {
                       scope.launch {
                           ClientManager.createCompleting(
                               employee.id,
                               availableTasks[index].second.id
                           )
                           availableTasks.clear()
                           ClientManager.getAllDepartmentTask(employee.departmentId).forEach{
                               availableTasks.add(it.key to it.value)
                           }
                       }
                    }
                )
            }
            if (employee.accessLevel == Titles.Manager.text){
                item{
                    IconButton(
                        onClick = {
                            isCreatingDialogOpen.value = true
                        }
                    ){
                        Icon(Icons.Filled.Add, "")
                    }
                }
            }
        }
        AnimatedVisibility (isFormVisible){
            key(selectedIndex){
                var name by remember{
                    mutableStateOf(availableTasks[selectedIndex].second.name)
                }
                var description by remember{
                    mutableStateOf(availableTasks[selectedIndex].second.description)
                }
                var amount by remember{
                    mutableDoubleStateOf(availableTasks[selectedIndex].second.amount)
                }
                var endingDate by remember{
                    mutableStateOf(availableTasks[selectedIndex].second.endingDate)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(
                        rememberScrollState()
                    ),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        TextField(
                            value = name,
                            onValueChange = {
                                name = it
                            },
                            label = {Text("Название")},
                            modifier = Modifier.width(150.dp)
                        )
                        TextField(
                            value = description,
                            onValueChange = {
                                description = it
                            },
                            label = {Text("Описание")},
                            modifier = Modifier.width(150.dp)
                        )
                    }
                    Column {
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
                        val isAdd = remember {
                            mutableStateOf(true)
                        }
                        Text(text = endingDate.toLocalDate().toString())
                        Checkbox(isAdd.value, onCheckedChange = {isAdd.value = !isAdd.value})
                        Column {
                            Button(
                                onClick = {
                                    endingDate = if (isAdd.value) {
                                        endingDate.plusDays(1)
                                    }else {
                                        endingDate.minusDays(1)
                                    }
                                }
                            ){
                                Text(text = "День")
                            }
                            Button(
                                onClick = {
                                    endingDate = if (isAdd.value) {
                                        endingDate.plusWeeks(1)
                                    }else{
                                        endingDate.minusWeeks(1)
                                    }
                                }
                            ){
                                Text(text = "Неделя")
                            }
                            Button(
                                onClick = {
                                    endingDate = if (isAdd.value) {
                                        endingDate.plusMonths(1)
                                    }else{
                                        endingDate.minusMonths(1)
                                    }
                                }
                            ){
                                Text(text = "Месяц")
                            }
                        }
                    }
                    Column {
                        IconButton({
                            scope.launch {
                                ClientManager.editingTask(
                                    Task(
                                        id = availableTasks[selectedIndex].second.id,
                                        name = name,
                                        description = description,
                                        amount = amount,
                                        creatingDate =  availableTasks[selectedIndex].second.creatingDate,
                                        employeeId =  availableTasks[selectedIndex].second.employeeId,
                                        endingDate = endingDate
                                    )
                                )
                                availableTasks.clear()
                                ClientManager.getAllDepartmentTask(employee.departmentId).forEach{
                                    availableTasks.add(it.key to it.value)
                                }
                            }
                        }){
                            Icon(Icons.Filled.Done, "")
                        }

                        IconButton({
                            scope.launch {
                                ClientManager.deletingTask(
                                    availableTasks[selectedIndex].second.id
                                )
                                availableTasks.clear()
                                ClientManager.getAllDepartmentTask(employee.departmentId).forEach{
                                    availableTasks.add(it.key to it.value)
                                }
                            }
                        }){
                            Icon(Icons.Filled.Delete, "")
                        }
                    }
                }
            }
        }
        BottomNavBar(
            *bottomButtons.map { it.key to it.value }.toTypedArray(),
            modifier = Modifier.padding(10.dp)
        )
    }
}

fun String.tryParseDouble(): Boolean
    = try {
        toDouble()
        true
    }
    catch (ex: Exception){
        false
    }