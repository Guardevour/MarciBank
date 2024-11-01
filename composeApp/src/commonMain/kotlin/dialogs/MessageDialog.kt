package dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MessageDialog(value: MutableState<Boolean>, text: String){
    AlertDialog(
        onDismissRequest = {value.value = false},
        buttons = @Composable {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Button( onClick = {value.value = false }){
                    Text("OK")
                }
            }
        },
        modifier = Modifier.fillMaxSize(0.6f)
    )
}