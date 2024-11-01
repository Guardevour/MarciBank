@file:OptIn(ExperimentalMaterialApi::class)

package components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun <T> DropDown(
    isExpanded: MutableState<Boolean>,
    selectedIndex: MutableState<Int>,
    selectableCollection: Collection<T>,
    label: String,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = {
            isExpanded.value = !isExpanded.value
        },
        modifier = Modifier.then(modifier)
    ) {
        TextField(
            value = if (selectableCollection.isNotEmpty()) selectableCollection.elementAt(selectedIndex.value).toString() else "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded.value
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = {
                isExpanded.value = false
            }
        ) {
            selectableCollection.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex.value = selectableCollection.indexOf(selectionOption)
                        isExpanded.value = false
                    }
                ) {
                    Text(text = selectionOption.toString())
                }
            }
        }
    }
}