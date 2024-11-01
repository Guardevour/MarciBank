import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList

object FileManager{
    @Composable
    fun saveReport(data: SnapshotStateList<String>, isLastMonth: Boolean){
        save(data, isLastMonth)
    }
}

@Composable
expect fun save(data: SnapshotStateList<String>, isLastMonth: Boolean)