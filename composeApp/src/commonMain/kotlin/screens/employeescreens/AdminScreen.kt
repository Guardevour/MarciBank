package screens.employeescreens

import ClientManager
import Titles
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dialogs.NewEmployeeDialog
import kotlinx.coroutines.launch
import models.Employee
import screens.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminScreen(
    employee: Employee
) {
    val employees : SnapshotStateList<Employee> = SnapshotStateList()
    val scope = rememberCoroutineScope()
    val isNewEmployeeDialogOpen = remember {
        mutableStateOf(false)
    }
    SideEffect {
        scope.launch {
            employees.clear()
            ClientManager.getAllEmployees().forEach {
                if (it.accessLevel != Titles.Admin.text){
                    employees.add(it)
                }
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
    LazyColumn(
        modifier = Modifier.fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Text("Сотрудники", textAlign = TextAlign.Center,modifier = Modifier.background(MaterialTheme.colors.surface).fillMaxWidth())
        }
        items(employees.size){index->
            employees[index].draw(Modifier.padding(10.dp)){
                scope.launch {
                    employees.clear()
                    ClientManager.getAllEmployees().forEach {
                        if (it.accessLevel != Titles.Admin.text){
                            employees.add(it)
                        }
                    }
                }
            }
        }
        item{
            IconButton(
                onClick = {
                   isNewEmployeeDialogOpen.value = true
                }
            ){
                Icon(Icons.Filled.Add, "")
            }
        }
    }
    if (isNewEmployeeDialogOpen.value){
        NewEmployeeDialog(
            value = isNewEmployeeDialogOpen
        ){
            scope.launch {
                employees.clear()
                ClientManager.getAllEmployees().forEach {
                    if (it.accessLevel != Titles.Admin.text){
                        employees.add(it)
                    }
                }
            }
        }
    }
}