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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.reading_now.ReadingNowState
import ua.acclorite.book_story.ui.common.components.book.PhysicalBookCover
import ua.acclorite.book_story.ui.common.components.common.StyledText

internal val Paper = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
internal val Rule = androidx.compose.ui.graphics.Color(0xFFE3DFD8)
internal val AppleInk = androidx.compose.ui.graphics.Color(0xFF1C1B19)
internal val AppleInkSoft = androidx.compose.ui.graphics.Color(0xFF8A8580)

@Composable
fun ReadingNowContent(
    state: ReadingNowState,
    navigateToReader: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToBookInfo: (Int) -> Unit
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
            AppleScreenHeader(
                title = stringResource(id = R.string.reading_now_screen),
                navigateToSettings = navigateToSettings
            )

            when (val book = state.currentBook) {
                null -> {
                    if (!state.isLoading) {
                        StyledText(
                            text = stringResource(id = R.string.reading_now_empty),
                            style = MaterialTheme.typography.bodyLarge.copy(color = AppleInkSoft),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp)
                        )
                    }
                }

                else -> {
                    Spacer(modifier = Modifier.height(6.dp))

                    OpenBookCard(
                        leftPage = state.leftPage,
                        rightPage = state.rightPage,
                        isLoading = state.isLoadingPages,
                        onClick = { navigateToReader(book.id) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StyledText(
                        text = book.title,
                        maxLines = 2,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = AppleInk,
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StyledText(
                            text = "${book.progress.calculateProgress(1)}%",
                            style = MaterialTheme.typography.bodyMedium.copy(color = AppleInkSoft)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = null,
                            tint = AppleInkSoft,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { navigateToBookInfo(book.id) }
                                .padding(4.dp)
                                .size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Rule
            )

            StyledText(
                text = stringResource(id = R.string.reading_now_recently_opened),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = AppleInk,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
            )

            if (state.recentBooks.isEmpty()) {
                StyledText(
                    text = stringResource(id = R.string.reading_now_empty),
                    style = MaterialTheme.typography.bodyMedium.copy(color = AppleInkSoft),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    items(state.recentBooks, key = { it.id }) { recent ->
                        RecentBookItem(
                            book = recent,
                            onClick = { navigateToReader(recent.id) },
                            onMore = { navigateToBookInfo(recent.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun AppleScreenHeader(
    title: String,
    navigateToSettings: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp, top = 18.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StyledText(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = AppleInk,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            trailing?.invoke()

            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Rule)
                    .clickable { navigateToSettings() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = AppleInkSoft,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(22.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Rule
        )
    }
}

@Composable
private fun RecentBookItem(book: Book, onClick: () -> Unit, onMore: () -> Unit) {
    Column(
        modifier = Modifier.width(102.dp)
    ) {
        PhysicalBookCover(
            uri = book.coverImage,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clickable(onClick = onClick)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
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
