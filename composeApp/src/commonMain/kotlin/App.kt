import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import mbank.composeapp.generated.resources.Res
import mbank.composeapp.generated.resources.compose_multiplatform
import mbank.composeapp.generated.resources.icon
import models.Department
import screens.*

val primaryDark = Color(0xFFFFB68C)
val onPrimaryDark = Color(0xFF532200)
val secondaryDark = Color(0xFFFFB68C)
val onSecondaryDark = Color(0xFF522300)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val backgroundDark = Color(0xFF1A120D)
val onBackgroundDark = Color(0xFFF0DFD7)
val surfaceDark = Color(0xFF363536)
val onSurfaceDark = Color(0xFFFFB68C)

@Composable
@Preview
fun App() {
    MaterialTheme(
        colors= darkColors(
                primary = primaryDark,
                onPrimary = onPrimaryDark,
                secondary = secondaryDark,
                onSecondary = onSecondaryDark,
                error = errorDark,
                onError = onErrorDark,
                background = backgroundDark,
                onBackground = onBackgroundDark,
                surface = surfaceDark,
                onSurface = onSurfaceDark,
        ),
    ) {
        Surface {
            val navHostController = rememberNavController()
            NavHost(navController = navHostController, startDestination = "auth") {
                composable("auth") {
                    AuthScreen(navHostController)
                }
                composable(
                    "employee/{employee}",
                    arguments = listOf(
                        navArgument("employee") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("employee")?.let { employee ->
                        EmployeeScreen(
                            navHostController,
                            employee
                        )
                    }
                }
                composable(
                    "tasks/{employee}",
                    arguments = listOf(
                        navArgument("employee") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("employee")?.let { employee ->
                        TasksScreen(
                            navHostController,
                            employee,
                        )
                    }
                }
                composable(
                    "audition/{employee}",
                    arguments = listOf(
                        navArgument("employee") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("employee")?.let { employee ->
                        AuditionScreen(
                            navHostController,
                            employee,
                        )
                    }
                }
                composable(
                    "penalty/{employee}"
                ) {backStackEntry ->
                    backStackEntry.arguments?.getString("employee")?.let { employee ->
                        PenaltyScreen(
                            navHostController,
                            employee
                        )
                    }
                }
                composable(
                    "penaltings/{employee}"
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("employee")?.let { employee ->
                        PenaltingScreen(
                            navHostController,
                            employee,
                        )
                    }
                }
            }
        }

    }
}


