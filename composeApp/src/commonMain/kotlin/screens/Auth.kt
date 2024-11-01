package screens

import ClientManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import dialogs.ErrorDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mbank.composeapp.generated.resources.Res
import mbank.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun AuthScreen(
    navHostController: NavHostController
){
    Screen {
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val isError = remember { mutableStateOf(false) }
        Image(
            imageResource(Res.drawable.icon), ""
        )

        TextField(
            label = { Text("Логин") },
            value = login,
            placeholder = { Text("Введите логин") },
            singleLine = true,
            onValueChange = {newLogin->
                if (newLogin.length <= 10){
                    login = newLogin.trim().lowercase()
                }

            }
        )

        TextField(
            label = { Text("Пароль") },
            value = password,
            placeholder = { Text("Введите пароль") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(Icons.Filled.Info, "show pass")
                }
            },
            onValueChange = {newPass->
                if (newPass.length <= 10){
                    password = newPass.trim()
                }
            }
        )

        Button(onClick = {
            scope.launch {
                try{
                    ClientManager.auth(login, password).let {
                        navHostController.navigate("employee/${Json.encodeToString(it)}")
                    }
                }
                catch (ex: Exception){
                     isError.value = true
                }

            }
        }){
            Text("Войти")
        }
        if (isError.value){
            ErrorDialog(isError)
        }
    }
}