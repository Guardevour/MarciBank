package screens.employeescreens

import Titles
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import components.BasicHalfScreenBlock
import kotlinx.coroutines.launch
import models.Completing
import models.Employee
import screens.fillTasksList

@Composable
fun EmployeeAndManagerScreen(
    employee: Employee
){
    var department by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val tasks: SnapshotStateList<Pair<String, Completing>> = SnapshotStateList()


    SideEffect {
        scope.launch {
            department = ClientManager.getDepartmentName(employee.departmentId)
            fillTasksList(employee, tasks)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ){

        BasicHalfScreenBlock(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(150.dp),
            content = {
                Text(employee.surname)
                Text(employee.name)
                Text(employee.fathername)

            }
        )
        BasicHalfScreenBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            content = {
                Text(employee.accessLevel)
                Text(department)
                Spacer(modifier = Modifier.height(15.dp))
                if (employee.accessLevel == Titles.Employee.text){
                    Text("Премиальные: ${employee.bonusMoney}р")
                }
            }
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxHeight(0.5f)
    ) {
        items(tasks.size){index->
            tasks[index].second.draw(
                tasks[index].first
            )
            {
                fillTasksList(employee, tasks)
            }
        }
    }

}