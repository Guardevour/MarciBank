package dialogs

import ClientManager
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.DropDown
import kotlinx.coroutines.launch
import models.Department
import models.Employee

@Composable
fun EmployeeDialog(value: MutableState<Boolean>, employee: Employee, refreshLambda: suspend ()->Unit) {
    AlertDialog(
        onDismissRequest = {value.value = false},
        buttons = {
            Column(
                modifier = Modifier.padding(10.dp).fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var surname by remember {
                    mutableStateOf(employee.surname)
                }
                var name by remember {
                    mutableStateOf(employee.name)
                }
                var fathername by remember {
                    mutableStateOf(employee.fathername)
                }
                var login by remember {
                    mutableStateOf(employee.login)
                }
                var password by remember {
                    mutableStateOf(employee.password)
                }
                val scope = rememberCoroutineScope()
                val departments = SnapshotStateList<Department>()
                val isExpanded = remember {
                    mutableStateOf(false)
                }
                val selectedIndex = remember {
                    mutableIntStateOf(0)
                }

                SideEffect {
                    scope.launch {
                        for ((index, department) in ClientManager.getAllDepartments().withIndex()) {
                            departments.add(department)
                            if (department.id == employee.departmentId){
                                selectedIndex.intValue = index
                                }
                            }
                        }
                }

                TextField(
                    value = surname,
                    onValueChange = {surname = it},
                    singleLine = false,
                    label = { Text("Фамилия") },
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = name,
                    onValueChange = {name = it},
                    singleLine = false,
                    label = { Text("Имя") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = fathername,
                    onValueChange = {fathername = it},
                    singleLine = false,
                    label = { Text("Отчество") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = login,
                    onValueChange = {login = it},
                    singleLine = false,
                    label = { Text("Логин") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = password,
                    onValueChange = {password = it},
                    singleLine = false,
                    label = { Text("Пароль") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )

                DropDown(
                    isExpanded = isExpanded,
                    label = "Отделение",
                    selectableCollection = departments,
                    selectedIndex = selectedIndex,
                    modifier = Modifier.padding(10.dp)
                )

                Button(
                    onClick = {
                        scope.launch {
                            ClientManager.editEmployee(
                                id = employee.id,
                                name = name,
                                surname = surname,
                                fathername = fathername,
                                accessLevel = employee.accessLevel,
                                bonusMoney =  employee.bonusMoney,
                                departmentId = departments[selectedIndex.intValue].id,
                                login = login,
                                password = password
                            )
                            value.value = false
                            refreshLambda()
                        }
                    }
                ){
                    Text("Сохранить")
                }

            }
        },
        modifier = Modifier.fillMaxSize(0.8f)
    )
}