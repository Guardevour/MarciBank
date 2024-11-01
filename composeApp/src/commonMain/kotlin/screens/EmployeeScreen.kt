package screens

import ClientManager
import Statuses
import Titles
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import components.BasicHalfScreenBlock
import components.BottomNavBar
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Completing
import models.Employee
import models.Task
import screens.employeescreens.AdminScreen
import screens.employeescreens.DirectorScreen
import screens.employeescreens.EmployeeAndManagerScreen

@Composable
fun EmployeeScreen(
    navHostController: NavHostController,
    employeeJson: String
) {

   Screen {
       val employee = Json.decodeFromString<Employee>(employeeJson)
       val bottomButtons = mutableMapOf<ImageVector, () -> Unit>()
       when(employee.accessLevel){
           Titles.Employee.text, Titles.Manager.text -> {
               EmployeeAndManagerScreen(employee)
               bottomButtons.putAll(
                   mapOf(
                       Icons.Filled.Person to {
                           navHostController.navigate("employee/${employeeJson}")
                       },
                       Icons.Filled.Menu to {
                           navHostController.navigate("tasks/${employeeJson}")
                       },
                       Icons.AutoMirrored.Filled.ExitToApp to {
                           navHostController.navigate("auth")
                       },
                       Icons.Filled.Warning to {
                           navHostController.navigate("penaltings/${employeeJson}")
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
           }
           Titles.Admin.text -> {
               AdminScreen(employee)
               bottomButtons.putAll(
                   mapOf(
                       Icons.Filled.Close to {
                           navHostController.navigate("auth")
                       }

                   )
               )
           }
           Titles.Director.text ->{
               DirectorScreen(
                   employee
               )
               bottomButtons.putAll(
                   mapOf(
                       Icons.Filled.Close to {
                           navHostController.navigate("auth")
                       }
                   )
               )
           }
       }


       BottomNavBar(
          buttons = bottomButtons.map { it.key to it.value }.toTypedArray(),
           modifier = Modifier.padding(10.dp)
       )
   }
}



suspend fun fillTasksList(employee: Employee, tasks: SnapshotStateList<Pair<String,Completing>>){
    val receivedTasks = ClientManager.getEmployeeCompleting(employee.id)

    tasks.clear()

    receivedTasks.forEach {
        tasks.add(it.key to it.value)
    }
}


