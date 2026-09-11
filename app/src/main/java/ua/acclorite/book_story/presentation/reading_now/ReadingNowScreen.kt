/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reading_now

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.presentation.reader.ReaderScreen
import ua.acclorite.book_story.ui.navigator.LocalNavigator
import ua.acclorite.book_story.ui.reading_now.ReadingNowContent

@Parcelize
object ReadingNowScreen : Screen, Parcelable {

    @Composable
    override fun Content() {
        val model = hiltViewModel<ReadingNowModel>()
        val state = model.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            model.refresh()
        }

        ReadingNowContent(
            state = state.value,
            navigateToReader = { bookId ->
                HistoryScreen.insertHistoryChannel.trySend(bookId)
                navigator.push(ReaderScreen(bookId = bookId))
            }
        )
    }
}
