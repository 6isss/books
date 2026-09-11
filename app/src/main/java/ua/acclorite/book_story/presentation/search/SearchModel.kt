/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.use_case.book.SearchBooksUseCase
import javax.inject.Inject

@HiltViewModel
class SearchModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        search("")
    }

    fun refresh() {
        search(_state.value.query)
    }

    fun search(query: String) {
        _state.update { it.copy(query = query, isLoading = true) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(150)

            val books: List<Book> = searchBooksUseCase("")
            val filtered = if (query.isBlank()) {
                books
            } else {
                books.filter { book ->
                    book.title.contains(query, ignoreCase = true) ||
                        book.author.getAsString()?.contains(query, ignoreCase = true) == true
                }
            }

            _state.update {
                it.copy(
                    books = filtered.sortedBy { book -> book.title.trim().lowercase() },
                    isLoading = false
                )
            }
        }
    }
}
