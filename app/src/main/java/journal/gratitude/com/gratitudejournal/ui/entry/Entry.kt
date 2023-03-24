package journal.gratitude.com.gratitudejournal.ui.entry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.presently.ui.CalmColors
import com.presently.ui.OriginalColors
import com.presently.ui.PresentlyTheme
import com.presently.ui.isDark
import journal.gratitude.com.gratitudejournal.R
import journal.gratitude.com.gratitudejournal.util.toFullString
import journal.gratitude.com.gratitudejournal.util.toStringWithDayOfWeek
import org.threeten.bp.LocalDate
import kotlin.random.Random

@Composable
fun Entry(
    onEntrySaved: (milestoneNumber: Int?) -> Unit,
    onShareClicked: (date: String, content: String) -> Unit
) {
    val viewModel = hiltViewModel<EntryyViewModel>()
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.logScreenView()
    }

    if (state.value.isSaved) {
        val milestoneNumber = if (state.value.milestoneWasReached) state.value.entryCount else null
        onEntrySaved(milestoneNumber)
        viewModel.onSaveHandled()
    }

    PresentlyTheme(
        selectedTheme = viewModel.getSelectedTheme()
    ) {
        EntryContent(
            state = state.value,
            onTextChanged = viewModel::onTextChanged,
            onSaveClicked = viewModel::saveEntry,
            onHintClicked = viewModel::changeHint,
            onShareClicked = onShareClicked
        )
    }
}

//todo show warning dialog if the entry is not saved

@Composable
fun EntryContent(
    modifier: Modifier = Modifier,
    state: EntryViewState,
    onTextChanged: (newText: String) -> Unit,
    onHintClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onShareClicked: (date: String, content: String) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !PresentlyTheme.colors.entryBackground.isDark()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = PresentlyTheme.colors.entryBackground
    ) {
        Column(
            modifier = modifier.windowInsetsPadding(WindowInsets.statusBars),
        ) {
            Text(
                text = when (state.date) {
                    LocalDate.now() -> stringResource(R.string.today)
                    LocalDate.now().minusDays(1) -> stringResource(R.string.yesterday)
                    else -> state.date.toStringWithDayOfWeek()
                },
                style = PresentlyTheme.typography.titleLarge,
                color = PresentlyTheme.colors.entryDate
            )
            Text(
                text = if (state.date == LocalDate.now()) stringResource(R.string.iam) else stringResource(R.string.iwas),
                style = PresentlyTheme.typography.titleLarge,
                color = PresentlyTheme.colors.entryDate
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.content,
                onValueChange = {
                    onTextChanged(it)
                },
                placeholder = {
                    val hintNumber = state.promptNumber
                    if (hintNumber == -1) {
                        Text(
                            text = if (state.date == LocalDate.now()) {
                                stringResource(id = R.string.what_are_you_thankful_for)
                            } else {
                                stringResource(id = R.string.what_were_you_thankful_for)
                            },
                            color = PresentlyTheme.colors.entryHint
                        )
                    } else {
                        //todo this is lost on rotation
                        val hints = stringArrayResource(id = R.array.prompts)
                        hints.shuffle()
                        Text(
                            text = hints[hintNumber % hints.size],
                            color = PresentlyTheme.colors.entryHint
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                textStyle = PresentlyTheme.typography.bodyMedium,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = PresentlyTheme.colors.entryBackground,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    textColor = PresentlyTheme.colors.entryBody,
                    cursorColor = PresentlyTheme.colors.debugColor1
                ),
            )
            Row() {
                if (state.shouldShowHintButton) {
                    IconButton(onClick = { onHintClicked() }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = stringResource(R.string.get_a_new_prompt),
                            tint = PresentlyTheme.colors.entryButtonBackground
                        )
                    }
                } else {
                    IconButton(onClick = {
                        onShareClicked(
                            state.date.toFullString(),
                            state.content
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share_your_gratitude),
                            tint = PresentlyTheme.colors.entryButtonBackground
                        )
                    }
                }
                Button(
                    onClick = { onSaveClicked() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = PresentlyTheme.colors.entryButtonBackground,
                        contentColor = PresentlyTheme.colors.entryButtonText
                    )
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
            val quotes = stringArrayResource(id = R.array.inspirations)
            val randomValue: Int = remember { Random.nextInt(quotes.size) }
            Text(
                text = quotes[randomValue],
                style = PresentlyTheme.typography.bodyExtraSmall,
                color = PresentlyTheme.colors.entryQuoteText
            )
            //todo make this have long press to copy to clipboard
            //todo hide this if the user says so
        }
    }
}