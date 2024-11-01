package components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberPicker(
    numberRange: IntRange,
    selectedValueState: MutableIntState,
    label: String
) {
    if (!numberRange.isEmpty()){
        var selectedIndex by remember {
            mutableIntStateOf(
               0
            )
        }
        Row(
            modifier = Modifier.height(100.dp)
        ) {
            TextField(
                value = numberRange.elementAt(selectedIndex).toString(),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.width
                (
                    (
                        numberRange.last.toString().length * 25
                    ).dp
                ),
                label = { Text(label, fontSize = 14.sp)},
                textStyle = TextStyle(fontSize = 12.sp)
            )
            Column {
                IconButton(
                    onClick = {
                        if (selectedIndex < numberRange.indexOf(numberRange.last)) {
                            selectedIndex++
                            selectedValueState.value = numberRange.elementAt(selectedIndex)
                        }
                    }
                ){
                    Icon(Icons.Filled.KeyboardArrowUp, "")
                }
                IconButton(
                    onClick = {
                        if (selectedIndex > 0){
                            selectedIndex--
                            selectedValueState.value = numberRange.elementAt(selectedIndex)
                        }
                    }
                ){
                    Icon(Icons.Filled.KeyboardArrowDown, "")
                }
            }
        }
    }
}