package dialogs


import ClientManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.DropDown
import kotlinx.coroutines.launch
import models.Department
import models.Employee
import models.Penalty
import screens.tryParseDouble

@Composable
fun NewPenaltingDialog(
    value: MutableState<Boolean>,
    departmentId: Int,
    refreshLambda: suspend ()-> Unit
) {
    AlertDialog(
        onDismissRequest = {value.value = false},
        buttons = @Composable {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val employeeIndex = remember{
                    mutableIntStateOf(0)
                }
                val penaltyIndex = remember{
                    mutableIntStateOf(0)
                }

                val employees = SnapshotStateList<Employee>()
                val penalties = SnapshotStateList<Penalty>()

                val isEmployeeExpanded = remember { mutableStateOf(false) }
                val isPenaltyExpanded = remember { mutableStateOf(false) }

                val scope = rememberCoroutineScope()
                SideEffect {
                   scope.launch {
                       employees.clear()
                       ClientManager.getAllDepEmployees(departmentId).forEach {
                           employees.add(it)
                       }
                       penalties.clear()
                       ClientManager.getPenalties("").forEach {
                           penalties.add(it)
                       }
                   }
                }

                Text("Оштрафовать сотрудника")

                DropDown(
                    isEmployeeExpanded,
                    selectedIndex = employeeIndex,
                    selectableCollection = employees,
                    label = "Сотрудник"
                )
                DropDown(
                    isPenaltyExpanded,
                    selectedIndex = penaltyIndex,
                    selectableCollection = penalties,
                    label = "Штраф"
                )

                Button(
                    onClick = {
                        scope.launch{
                            ClientManager.addNewPenalting(
                                employeeId = employees[employeeIndex.intValue].id,
                                penaltyId = penalties[penaltyIndex.intValue].id
                            )
                            refreshLambda()
                            value.value = false
                        }
                    }
                ){
                    Text("Создать")
                }
            }
        },
        modifier = Modifier.fillMaxSize(0.6f)
    )
}