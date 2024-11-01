import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.IOException
import java.time.LocalDateTime

@Composable
actual fun save(data: SnapshotStateList<String>, isLastMonth: Boolean) {
    val context = LocalContext.current
    val resolver = context.contentResolver

    val fileContent = data.joinToString{
        "|$it|\n"
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "Отчет по отделам ${if (isLastMonth) "за последний месяц" else "за всё время"} от ${LocalDateTime.now()}")
        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }

    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

    uri?.let { documentUri ->
        try {
            resolver.openOutputStream(documentUri)?.use { outputStream ->
                outputStream.write(fileContent.toByteArray())
                Toast.makeText(context, data.size.toString(), Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show()
        }
    }

}
