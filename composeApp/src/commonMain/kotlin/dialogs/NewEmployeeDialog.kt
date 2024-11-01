package dialogs

import androidx.compose.foundation.layout.Column
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

@Composable
fun NewEmployeeDialog(value: MutableState<Boolean>, refreshLambda : suspend () -> Unit) {
    AlertDialog(
        onDismissRequest = {value.value = false},
        buttons = {
            Column(
                modifier = Modifier.padding(10.dp).verticalScroll(rememberScrollState()).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var surname by remember {
                    mutableStateOf("")
                }
                var name by remember {
                    mutableStateOf("")
                }
                var fathername by remember {
                    mutableStateOf("")
                }
                var login by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }
                val scope = rememberCoroutineScope()
                val departments = SnapshotStateList<Department>()
                val isExpandedDepartments = remember {
                    mutableStateOf(false)
                }
                val selectedIndexDepartments = remember {
                    mutableIntStateOf(0)
                }

                val titles = Titles.entries.map {
                    it.text
                }
                val isExpandedTitles = remember {
                    mutableStateOf(false)
                }
                val selectedIndexTitles = remember {
                    mutableIntStateOf(0)
                }

                SideEffect {
                    scope.launch {
                        for ( department in ClientManager.getAllDepartments()) {
                            departments.add(department)
                        }
                    }
                }

                TextField(
                    value = surname,
                    onValueChange = {surname = it},
                    singleLine = true,
                    label = { Text("Фамилия") },
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = name,
                    onValueChange = {name = it},
                    singleLine = true,
                    label = { Text("Имя") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = fathername,
                    onValueChange = {fathername = it},
                    singleLine = true,
                    label = { Text("Отчество") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = login,
                    onValueChange = {login = it},
                    singleLine = true,
                    label = { Text("Логин") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = password,
                    onValueChange = {password = it},
                    singleLine = true,
                    label = { Text("Пароль") }
                    ,
                    modifier = Modifier.padding(10.dp)
                )

                DropDown(
                    isExpanded = isExpandedDepartments,
                    label = "Отделение",
                    selectableCollection = departments,
                    selectedIndex = selectedIndexDepartments,
                    modifier = Modifier.padding(10.dp)
                )
                DropDown(
                    isExpanded = isExpandedTitles,
                    label = "Уровень доступа",
                    selectableCollection = titles,
                    selectedIndex = selectedIndexTitles,
                    modifier = Modifier.padding(10.dp)
                )

                Button(
                    onClick = {
                        scope.launch {
                            ClientManager.addEmployee(
                                name = name,
                                surname = surname,
                                fathername = fathername,
                                accessLevel = titles[selectedIndexTitles.intValue],
                                bonusMoney =  0.0,
                                departmentId = departments[selectedIndexDepartments.intValue].id,
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