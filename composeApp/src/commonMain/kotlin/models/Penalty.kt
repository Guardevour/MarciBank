package models

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class Penalty(
    val id: Int,
    val name: String,
    val description: String,
    val amount: Double
){
    override fun toString(): String = name

    @Composable
    fun draw(
        modifier: Modifier = Modifier
    ){
        val isExpanded = remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier.then(modifier).clickable{
                isExpanded.value = !isExpanded.value
            },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("$name : $amount")
            AnimatedVisibility(
                isExpanded.value
            ){
                Text(description)
            }
        }
    }
}
