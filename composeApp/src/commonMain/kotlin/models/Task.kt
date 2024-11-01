package models

import ClientManager
import LocalDateSerializer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val employeeId: Int,
    val amount: Double,
    @Serializable(with = LocalDateSerializer::class)
    val creatingDate: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val endingDate: LocalDateTime
){
    @Composable
    fun draw(
        additionalOnClick: () -> Unit,
        createCompleting: () -> Unit
    ){
        val isExtended = remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
                .background(
                    MaterialTheme.colors.background,
                    RoundedCornerShape(10.dp)
                ).clickable {
                    isExtended.value = !isExtended.value
                    additionalOnClick()
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier =  Modifier.fillMaxWidth()
            ) {
                Text("$name $amount-рублей", fontSize = 16.sp)
                Button(
                    onClick = {
                        createCompleting()
                    },
                    modifier = Modifier.offset(50.dp)
                ){
                    Text("Взять")
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier =  Modifier.fillMaxWidth()
            ) {
                Text("Поставили: ${creatingDate.toLocalDate()}", fontSize = 11.sp)
                Text("Выполнить до: ${endingDate.toLocalDate()}", fontSize = 11.sp)

            }
            AnimatedVisibility(isExtended.value){
                Text(description, textAlign = TextAlign.Center, fontSize = 12.sp, modifier = Modifier.fillMaxWidth())
            }
        }
    }


}