/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.search

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.presentation.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.history.HistoryScreen
import ua.acclorite.book_story.presentation.navigator.Screen
import ua.acclorite.book_story.presentation.reader.ReaderScreen
import ua.acclorite.book_story.presentation.settings.SettingsScreen
import ua.acclorite.book_story.ui.navigator.LocalNavigator
import ua.acclorite.book_story.ui.search.SearchContent

@Parcelize
object SearchScreen : Screen, Parcelable {

    @Composable
    override fun Content() {
        val model = hiltViewModel<SearchModel>()
        val state = model.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            model.refresh()
        }

        SearchContent(
            state = state.value,
            onQueryChange = { model.search(it) },
            navigateToReader = { bookId ->
                HistoryScreen.insertHistoryChannel.trySend(bookId)
                navigator.push(ReaderScreen(bookId = bookId))
            },
            navigateToBookInfo = { bookId ->
                navigator.push(BookInfoScreen(bookId = bookId))
            },
            navigateToSettings = {
                navigator.push(SettingsScreen)
            }
        )
    }
}
