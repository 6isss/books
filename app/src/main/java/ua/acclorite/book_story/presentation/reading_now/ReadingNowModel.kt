/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reading_now

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.model.reader.ReaderText
import ua.acclorite.book_story.domain.use_case.book.GetTextUseCase
import ua.acclorite.book_story.domain.use_case.history.GetHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class ReadingNowModel @Inject constructor(
    private val getHistory: GetHistoryUseCase,
    private val getText: GetTextUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReadingNowState())
    val state = _state.asStateFlow()

    private var loadedBookId: Int? = null

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val books = getHistory("")
                .flatMap { grouped -> grouped.history }
                .sortedByDescending { history -> history.time }
                .map { history -> history.book }
                .distinctBy { book -> book.id }

            val current = books.firstOrNull()
            _state.update {
                it.copy(
                    currentBook = current,
                    recentBooks = books.drop(1).take(12),
                    isLoading = false,
                    isLoadingPages = current != null && current.id != loadedBookId
                )
            }

            if (current != null && current.id != loadedBookId) {
                loadPages(current)
            }
        }
    }

    private suspend fun loadPages(book: Book) {
        val lines = getText(book.id)
            .filterIsInstance<ReaderText.Text>()
            .map { text -> text.line.text.trim() }
            .filter { line -> line.isNotBlank() }

        val excerpt = when {
            lines.isNotEmpty() -> {
                val start = book.scrollIndex.coerceIn(0, (lines.lastIndex - 1).coerceAtLeast(0))
                lines.drop(start).take(8)
            }

            else -> book.description
                ?.split("\n")
                ?.map { line -> line.trim() }
                ?.filter { line -> line.isNotBlank() }
                .orEmpty()
                .take(8)
        }

        loadedBookId = book.id
        _state.update {
            it.copy(
                leftPage = excerpt.take(4),
                rightPage = excerpt.drop(4).take(4),
                isLoadingPages = false
            )
        }
    }
}
