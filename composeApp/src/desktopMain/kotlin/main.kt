import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mbank.composeapp.generated.resources.Res
import mbank.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MBank",
        icon = painterResource("drawable/icon.png")
    ) {
         App()

    }
}