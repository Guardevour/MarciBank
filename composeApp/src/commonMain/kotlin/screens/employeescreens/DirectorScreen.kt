package screens.employeescreens

import ClientManager
import FileManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dialogs.MessageDialog
import kotlinx.coroutines.launch
import models.Employee
import screens.Screen
import java.net.FileNameMap

@Composable
fun DirectorScreen(
    employee: Employee
){
    val departmentResults: SnapshotStateList<String> = SnapshotStateList()
    val scope = rememberCoroutineScope()
    val isLastMonth = remember{
        mutableStateOf(false)
    }
    val isReportUsed = remember{
        mutableStateOf(false)
    }
    val isDlgShowed = remember {
        mutableStateOf(false)
    }
    SideEffect {
      scope.launch  {
          ClientManager.departmentResults(isLastMonth = isLastMonth.value).forEach {
              departmentResults.add(it)
          }
      }
    }
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("${employee.surname} ${employee.name} ${employee.fathername}")
        Text(employee.accessLevel)
    }
    Column{
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth()
                .background(MaterialTheme.colors.background, RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                isLastMonth.value,
                {
                    isLastMonth.value = !isLastMonth.value
                }
            )
            Text("Отображать данные за последний месяц", modifier = Modifier.padding(10.dp))
        }
        Row{
            IconButton(
                onClick = {
                    isReportUsed.value = true
                }
            ){
                Icon(Icons.Filled.Share, "")
            }

             IconButton(
                 onClick = {
                     scope.launch {
                         ClientManager.giveSalary()
                         isDlgShowed.value = true

                     }
                 }
             ){
                 Icon(Icons.Filled.Refresh, "")
             }
        }

    }
    LazyColumn(
        modifier = Modifier.fillMaxHeight(0.5f)
    ) {
        items(departmentResults.size){
            Text(departmentResults[it])

        }
        item{
            if (isReportUsed.value){
                FileManager.saveReport(departmentResults, isLastMonth = isLastMonth.value)
                isReportUsed.value = false
            }
        }
    }
    if (isDlgShowed.value){
        MessageDialog(
            isDlgShowed,
            "Зарплата успешно выдана"
        )
    }
}