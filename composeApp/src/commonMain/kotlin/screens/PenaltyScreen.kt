package screens

import ClientManager
import Titles
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import components.BottomNavBar
import dialogs.NewPenaltyDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import models.Employee
import models.Penalty
import java.lang.ProcessBuilder.Redirect.to

@Composable
fun PenaltyScreen(
    navHostController: NavHostController,
    employeeJson : String
) {
    val employee = Json.decodeFromString<Employee>(employeeJson)
    val scope = rememberCoroutineScope()
    val searchStatus = remember {
        mutableStateOf("")
    }
    val penalties =  SnapshotStateList<Penalty>()
    val isNewPenaltyDialogOpen = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(searchStatus.value) {
        scope.launch {
            penalties.clear()
            ClientManager.getPenalties(searchStatus.value).forEach {
                penalties.add(it)
            }
        }
    }
    Screen {
        TextField(
            value = searchStatus.value,
            onValueChange = {
                searchStatus.value = it
            },
            label = {
                Text("Введите часть названия или описания", fontSize = 12.sp)
            },
            placeholder = {
                Text("Введите часть названия или описания", fontSize = 12.sp)
            }
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            items(penalties.size){
               penalties[it].draw()
            }
           if (employee.accessLevel == Titles.Manager.text){
               item{
                   IconButton(
                       onClick = {
                           isNewPenaltyDialogOpen.value = true
                       }
                   ){
                       Icon(Icons.Filled.Add, "")
                   }
               }
           }
        }
        if (isNewPenaltyDialogOpen.value){
            NewPenaltyDialog(
                value = isNewPenaltyDialogOpen
            ) {
                penalties.clear()
                ClientManager.getPenalties(searchStatus.value).forEach {
                    penalties.add(it)
                }
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