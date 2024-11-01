package screens

import ClientManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import dialogs.NewPenaltingDialog
import dialogs.NewPenaltyDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import models.Employee
import models.Penalting

@Composable
fun PenaltingScreen(
    navHostController: NavHostController,
    employeeJson: String
) {
    val employee = Json.decodeFromString<Employee>(employeeJson)
    val bottomButtons = mutableMapOf<ImageVector, () -> Unit>()
    val scope = rememberCoroutineScope()
    if (employee.accessLevel == Titles.Manager.text){
        val penaltings = SnapshotStateList<Pair<String, Penalting>>()

        val isNewPenaltingDialogOpen = remember{
            mutableStateOf(false)
        }

        SideEffect {
            scope.launch {
                penaltings.clear()
                ClientManager.getAllEmployeesPenaltings(employee.departmentId).forEach{
                    penaltings.add(
                        it.key to it.value
                    )
                }
            }
        }
        Screen {
            LazyColumn {
                 items(penaltings.size){index->
                     Column(
                         verticalArrangement = Arrangement.Center,
                         horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                         Text(penaltings[index].first.split('|')[1])
                         Text(penaltings[index].first.split('|')[0])
                         Text(penaltings[index].second.penaltyDate.toString())
                     }
                 }
                item {
                    IconButton(
                        onClick = {
                            isNewPenaltingDialogOpen.value = true
                        }
                    ){
                        Icon(Icons.Filled.Add, "")
                    }
                }
            }
            if (isNewPenaltingDialogOpen.value){
                NewPenaltingDialog(
                    value = isNewPenaltingDialogOpen,
                    departmentId = employee.departmentId,
                    refreshLambda = {
                        penaltings.clear()
                        ClientManager.getAllEmployeesPenaltings(employee.departmentId).forEach{
                            penaltings.add(
                                it.key to it.value
                            )
                        }
                    }
                )
            }

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

            BottomNavBar(
                *bottomButtons.map { it.key to it.value }.toTypedArray(),
                modifier = Modifier.padding(10.dp)
            )
        }
    }
    else{
        val penaltings = SnapshotStateList<Pair<String, Penalting>>()
        SideEffect {
            scope.launch {
                penaltings.clear()
                ClientManager.getEmployeesPenaltings(employee.id).forEach{
                    penaltings.add(
                       it.key to it.value
                    )
                }
            }
        }
        Screen {
            LazyColumn {
                items(penaltings.size){index->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(penaltings[index].first.split('|')[0])
                        Text(penaltings[index].second.penaltyDate.toString())
                    }
                }
            }

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

            BottomNavBar(
                *bottomButtons.map { it.key to it.value }.toTypedArray(),
                modifier = Modifier.padding(10.dp)
            )
        }
    }

}