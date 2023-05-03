package journal.gratitude.com.gratitudejournal.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.presently.ui.PresentlyTheme
import journal.gratitude.com.gratitudejournal.model.Entry
import journal.gratitude.com.gratitudejournal.util.toStringWithDayOfWeek
import kotlinx.datetime.LocalDate

@Composable
fun SearchResult(
    result: Entry,
    onEntryClicked: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onEntryClicked(result.entryDate)
            }
    ) {
        Text(
            text = result.entryDate.toStringWithDayOfWeek(),
            style = PresentlyTheme.typography.bodyLarge,
            color = PresentlyTheme.colors.timelineDate,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = result.entryContent,
            style = PresentlyTheme.typography.bodyMedium,
            color = PresentlyTheme.colors.timelineContent
        )
    }
}
