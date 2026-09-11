/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reading_now

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.library.Book

@Immutable
data class ReadingNowState(
    val currentBook: Book? = null,
    val recentBooks: List<Book> = emptyList(),

    val leftPage: List<String> = emptyList(),
    val rightPage: List<String> = emptyList(),

    val isLoading: Boolean = true,
    val isLoadingPages: Boolean = true
)
