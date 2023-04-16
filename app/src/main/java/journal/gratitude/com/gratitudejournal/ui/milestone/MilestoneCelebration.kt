package journal.gratitude.com.gratitudejournal.ui.milestone

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.presently.ui.JoyColors
import com.presently.ui.PresentlyColors
import com.presently.ui.PresentlyTheme
import journal.gratitude.com.gratitudejournal.R

@Composable
fun MilestoneCelebration(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onShareClicked: () -> Unit,
) {
    val viewModel = hiltViewModel<MilestoneViewModel>()
    val state = viewModel.state.collectAsState()

    MilestoneScreen(
        theme = viewModel.getSelectedTheme(),
        milestoneNumber = state.value.milestoneNumber,
        onDismiss = onDismiss,
        onShareClicked = {
            viewModel.onShareClicked()
            onShareClicked()
        }
    )
}

@Composable
fun MilestoneScreen(
    modifier: Modifier = Modifier,
    theme: PresentlyColors,
    milestoneNumber: Int,
    onDismiss: () -> Unit,
    onShareClicked: () -> Unit,
) {
    PresentlyTheme(
        selectedTheme = theme
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = PresentlyTheme.colors.timelineBackground
        ) {
            Column(
                modifier = modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$milestoneNumber",
                    fontSize = 128.sp,
                )
                Text(
                    text = "5 " + stringResource(R.string.days_of_gratitude),
                    style = PresentlyTheme.typography.titleLarge,
                    fontSize = 64.sp
                )
                Text(
                    text = stringResource(R.string.congrats_milestone)
                )
                Button(
                    onClick = { onShareClicked() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = PresentlyTheme.colors.timelineContent,
                        contentColor = PresentlyTheme.colors.timelineBackground,
                    ),
                ) {
                    Text(stringResource(R.string.share_your_achievement))
                }
                //todo feature idea -- if user hasn't turned on backups show button to export or to set up auto backup
            }
        }
    }
}