/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reading_now

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.reading_now.ReadingNowState
import ua.acclorite.book_story.ui.common.components.common.AsyncCoverImage
import ua.acclorite.book_story.ui.common.components.common.StyledText

private val Paper = Color(0xFFFBF8F2)
private val PaperDim = Color(0xFFEDE7DC)
private val Ink = Color(0xFF241F1A)
private val InkSoft = Color(0xFF7A6F62)

@Composable
fun ReadingNowContent(
    state: ReadingNowState,
    navigateToReader: (Int) -> Unit
) {
    Scaffold(
        containerColor = Paper
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            StyledText(
                text = stringResource(id = R.string.reading_now_screen),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Ink,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp
                ),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 18.dp)
            )

            when (val book = state.currentBook) {
                null -> {
                    if (!state.isLoading) {
                        StyledText(
                            text = stringResource(id = R.string.reading_now_empty),
                            style = MaterialTheme.typography.bodyLarge.copy(color = InkSoft),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp)
                        )
                    }
                }

                else -> {
                    OpenBookCard(
                        leftPage = state.leftPage,
                        rightPage = state.rightPage,
                        isLoading = state.isLoadingPages,
                        onClick = { navigateToReader(book.id) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    StyledText(
                        text = book.title,
                        maxLines = 2,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Ink,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    StyledText(
                        text = book.author.asString(),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium.copy(color = InkSoft),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(PaperDim)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(book.progress.coerceIn(0f, 1f).coerceAtLeast(0.01f))
                                .height(3.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Ink.copy(alpha = 0.7f))
                        )
                    }

                    StyledText(
                        text = "${book.progress.calculateProgress(1)}%",
                        style = MaterialTheme.typography.bodySmall.copy(color = InkSoft),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Ink)
                            .clickable { navigateToReader(book.id) }
                    ) {
                        StyledText(
                            text = stringResource(id = R.string.reading_now_continue),
                            style = MaterialTheme.typography.titleMedium.copy(color = Paper),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            if (state.recentBooks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(34.dp))

                StyledText(
                    text = stringResource(id = R.string.reading_now_recently_opened),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = InkSoft,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    items(state.recentBooks, key = { it.id }) { recent ->
                        RecentBookItem(
                            book = recent,
                            onClick = { navigateToReader(recent.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentBookItem(book: Book, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(104.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 7.dp, bottomEnd = 7.dp),
                    ambientColor = Color(0x33453A2C),
                    spotColor = Color(0x55453A2C)
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 3.dp,
                        bottomStart = 3.dp,
                        topEnd = 7.dp,
                        bottomEnd = 7.dp
                    )
                )
                .background(PaperDim)
        ) {
            val cover = book.coverImage
            if (cover != null) {
                AsyncCoverImage(
                    uri = cover,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(
                        id = R.string.cover_image_not_found_content_desc
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f),
                    tint = InkSoft.copy(alpha = 0.4f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            StyledText(
                text = "${book.progress.calculateProgress(1)}%",
                style = MaterialTheme.typography.bodySmall.copy(color = InkSoft)
            )
        }
    }
}
