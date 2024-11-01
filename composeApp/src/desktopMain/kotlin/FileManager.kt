import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
actual fun save(data: SnapshotStateList<String>, isLastMonth: Boolean) {

        File(getDocumentsFolderPath() +"\\MBank").mkdir()
        File(getDocumentsFolderPath() +"\\MBank\\${"Отчет по отделам ${if (isLastMonth) "за последний месяц" else "за всё время"} от ${LocalDate.now()} ${LocalDateTime.now().hour}-${LocalDateTime.now().minute}-${LocalDateTime.now().second}" }.txt").printWriter().use { out ->
            data.forEach {
                out.println(it)
            }
        }
}
fun getDocumentsFolderPath(): String {
    val osName = System.getProperty("os.name").lowercase()
    return when {
        osName.contains("win") -> System.getProperty("user.home") + "\\Documents"
        osName.contains("mac") -> System.getProperty("user.home") + "/Documents"
        osName.contains("nix") || osName.contains("nux") -> System.getProperty("user.home") + "/Documents"
        else -> ""
    }
}