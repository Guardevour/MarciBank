package dialogs

import ClientManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import screens.tryParseDouble

@Composable
fun NewPenaltyDialog(
    value: MutableState<Boolean>,
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
            var name by remember {
                mutableStateOf("")
            }
            var description by remember {
                mutableStateOf("")
            }
            var amount by remember {
                mutableStateOf(0.0)
            }
            val scope = rememberCoroutineScope()
            Text("Создать новый штраф")
            TextField(
                value = name,
                label = {
                    Text("Название")
                },
                onValueChange = {
                    name = it
                },
                singleLine = true,
                modifier = Modifier.padding(10.dp)
            )
            TextField( value = description,
                label = {
                    Text("Описание")
                },
                onValueChange = {
                    description = it
                },
                modifier = Modifier.padding(10.dp)
            )
            TextField(
                value = amount.toString(),
                label = {Text("Значение")},
                onValueChange = {
                    if (it.tryParseDouble()){
                        amount = it.toDouble()
                    }
                },
                singleLine = true,
                modifier = Modifier.padding(10.dp)
            )
            Button(
                onClick = {
                    scope.launch{
                        ClientManager.newPenalty(
                            name,
                            description,
                            amount
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