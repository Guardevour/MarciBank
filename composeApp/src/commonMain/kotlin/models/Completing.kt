package models

import ClientManager
import LocalDateSerializer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.BasicHalfScreenBlock
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Completing(
    val id: Int,
    val employeeId: Int,
    val taskId: Int,
    val status: String,
    @Serializable(with = LocalDateSerializer::class)
    val completingDate: LocalDateTime
){
    @Composable
    fun draw(name: String, updateLambda: suspend () -> Unit){
        val scope = rememberCoroutineScope()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    MaterialTheme.colors.background,
                    RoundedCornerShape(10.dp)
                )
        ){
            Text(name)
            Text(status)

            IconButton(
                onClick = {
                    scope.launch {
                        ClientManager.changeCompletingStatus(
                            this@Completing,
                            Statuses.InAudition
                        )
                        updateLambda()
                    }
                }
            ){
                Icon(Icons.Filled.Done, "")
            }
            IconButton(
                onClick = {
                    scope.launch {
                        ClientManager.changeCompletingStatus(
                            this@Completing,
                            Statuses.Aborted
                        )
                        updateLambda()
                    }
                }
            ){
                Icon(Icons.Filled.Close, "")
            }
        }
    }
    @Composable
    fun drawAudition(
        onSubmit: suspend() -> Unit,
        onCancel: suspend() -> Unit,
        taskName: String
    ){

        var employee: Employee? by remember {
            mutableStateOf(null)
        }

        val scope = rememberCoroutineScope()
        SideEffect{
            scope.launch {
                employee = ClientManager.getEmployee(employeeId)

            }
        }
       if(employee != null){
          Column(
              verticalArrangement = Arrangement.SpaceEvenly,
              horizontalAlignment = Alignment.CenterHorizontally
          ) {
             Row{
                 BasicHalfScreenBlock(
                     content = {
                         Column {
                             Text(employee!!.surname)
                             Text(employee!!.name)
                             Text(employee!!.fathername)
                         }
                     },
                     modifier = Modifier.fillMaxWidth(0.5f)
                 )
                 BasicHalfScreenBlock(
                     content = {
                         Column {
                             Text(taskName)
                             Text(completingDate.toString())

                         }
                     },
                     modifier = Modifier.fillMaxWidth()
                 )
             }
              Row {
                  IconButton(
                      onClick = {
                          scope.launch {
                              ClientManager.changeMoney(
                                  employee!!.id,
                                  taskName.split('|')[1].toDouble()
                              )
                              ClientManager.changeCompletingStatus(
                                  this@Completing,
                                  Statuses.Completed
                              )
                            onSubmit()
                          }
                      }
                  ){
                      Icon(Icons.Filled.Done, "")
                  }
                  IconButton(
                    onClick = {
                        scope.launch{
                            ClientManager.changeCompletingStatus(
                                this@Completing,
                                Statuses.InCompleting
                            )
                            onCancel()
                        }
                    }
                  ){
                      Icon(Icons.Filled.Close, "")
                  }
              }
          }
       }
    }
}