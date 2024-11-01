package screens

import ClientManager
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import components.BottomNavBar
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import models.Completing
import models.Employee
import models.Task

@Composable
fun AuditionScreen(
    navHostController: NavHostController,
    employeeJson: String
){
    val employee = Json.decodeFromString<Employee>(employeeJson)
    val completedTasks: SnapshotStateList<Pair<String, Completing>> = SnapshotStateList()
    val scope = rememberCoroutineScope()
    SideEffect {
        scope.launch {
            completedTasks.clear()
            ClientManager.getAuditionDepartmentTask(employee.departmentId).forEach{
                completedTasks.add(it.key to it.value)
            }

        }
    }
    Screen {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(completedTasks.size) { index ->
                completedTasks[index].second.drawAudition(
                    onSubmit = {
                        scope.launch {
                            completedTasks.clear()
                            ClientManager.getAuditionDepartmentTask(employee.departmentId).forEach{
                                completedTasks.add(it.key to it.value)
                            }
                        }
                    },
                    onCancel = {
                        scope.launch {
                            completedTasks.clear()
                            ClientManager.getAuditionDepartmentTask(employee.departmentId).forEach{
                                completedTasks.add(it.key to it.value)
                            }
                        }
                    },
                    taskName = completedTasks[index].first
                )
            }
        }

        BottomNavBar(
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
            },
            Icons.Filled.CheckCircle to {
                navHostController.navigate("audition/${employeeJson}")
            },
            Icons.Filled.Notifications to {
                navHostController.navigate("penalty/${employeeJson}")
            },
            modifier = Modifier.padding(10.dp)
        )
    }

}