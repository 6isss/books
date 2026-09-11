/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.search.SearchState
import ua.acclorite.book_story.ui.common.components.book.PhysicalBookCover
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.reading_now.AppleInk
import ua.acclorite.book_story.ui.reading_now.AppleInkSoft
import ua.acclorite.book_story.ui.reading_now.AppleScreenHeader
import ua.acclorite.book_story.ui.reading_now.Paper
import ua.acclorite.book_story.ui.reading_now.Rule

@Composable
fun SearchContent(
    state: SearchState,
    onQueryChange: (String) -> Unit,
    navigateToReader: (Int) -> Unit,
    navigateToBookInfo: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {
    Scaffold(containerColor = Paper) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppleScreenHeader(
                title = stringResource(id = R.string.search_screen),
                navigateToSettings = navigateToSettings
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Rule.copy(alpha = 0.55f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = AppleInkSoft,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (state.query.isEmpty()) {
                        StyledText(
                            text = stringResource(id = R.string.search_placeholder),
                            style = MaterialTheme.typography.bodyLarge.copy(color = AppleInkSoft)
                        )
                    }
                    BasicTextField(
                        value = state.query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = AppleInk),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (!state.isLoading && state.books.isEmpty()) {
                StyledText(
                    text = stringResource(id = R.string.search_empty),
                    style = MaterialTheme.typography.bodyMedium.copy(color = AppleInkSoft),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalArrangement = Arrangement.spacedBy(26.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.books, key = { it.id }) { book ->
                    SearchBookItem(
                        book = book,
                        onClick = { navigateToReader(book.id) },
                        onMore = { navigateToBookInfo(book.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBookItem(book: Book, onClick: () -> Unit, onMore: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PhysicalBookCover(
            uri = book.coverImage,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clickable(onClick = onClick)
        )

        Spacer(modifier = Modifier.height(10.dp))

        StyledText(
            text = book.title,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = AppleInk,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StyledText(
                text = "${book.progress.calculateProgress(1)}%",
                style = MaterialTheme.typography.bodySmall.copy(color = AppleInkSoft)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                tint = AppleInkSoft,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onMore() }
                    .size(18.dp)
            )
        }
    }
}
