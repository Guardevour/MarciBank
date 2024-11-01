package models

import ClientManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dialogs.EmployeeDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Int,
    val surname: String,
    val name: String,
    val fathername: String,
    val login: String,
    val password: String,
    val accessLevel: String,
    val bonusMoney: Double,
    val departmentId: Int
){

    override fun toString(): String = "$surname $name $fathername"

    @Composable
    fun draw(
        modifier: Modifier = Modifier,
        onDeleteClick: suspend ()->Unit,
    ){
        val isExpanded = remember {
            mutableStateOf(false)
        }
        if (isExpanded.value){
            EmployeeDialog(
                value = isExpanded,
                employee = this,
                onDeleteClick
            )
        }
        val scope = rememberCoroutineScope()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().then(modifier).background(MaterialTheme.colors.background, RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(surname)
                    Text(name)
                    Text(fathername)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(login)
                    Text(password)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(accessLevel)
                    Text(bonusMoney.toString())
                }
            }
            Column {
                IconButton(
                    onClick = {
                       scope.launch {
                           ClientManager.deleteEmployee(id)
                           onDeleteClick()
                       }
                    }
                ){
                    Icon(Icons.Filled.Delete, "")
                }
                IconButton(
                    onClick = {
                        isExpanded.value = true
                    }
                ){
                    Icon(Icons.Filled.Edit, "")
                }
            }
        }
    }
}
