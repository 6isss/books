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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.ui.common.components.common.StyledText

private val PaperLight = Color(0xFFFFFFFF)
private val PaperShade = Color(0xFFF1ECE3)
private val CoverBoard = Color(0xFF141210)
private val Ink = Color(0xFF2A241D)

/**
 * A 3D open-book preview: a dark cover board holding two white pages side by side,
 * a central spine crease, curved page edges and a warm floating shadow.
 * Shows real text from the book.
 */
@Composable
fun OpenBookCard(
    leftPage: List<String>,
    rightPage: List<String>,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.32f)
            .shadow(
                elevation = 22.dp,
                shape = RoundedCornerShape(6.dp),
                ambientColor = Color(0x33000000),
                spotColor = Color(0x59000000)
            )
            .clip(RoundedCornerShape(6.dp))
            .background(CoverBoard)
            .clickable(onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 9.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            BookPage(
                lines = leftPage,
                isLoading = isLoading,
                isRight = false,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(1.dp))

            BookPage(
                lines = rightPage,
                isLoading = isLoading,
                isRight = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        // central spine crease
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(26.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x00000000),
                            Color(0x14000000),
                            Color(0x40000000),
                            Color(0x14000000),
                            Color(0x00000000)
                        )
                    )
                )
        )
    }
}

@Composable
private fun BookPage(
    lines: List<String>,
    isLoading: Boolean,
    isRight: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = if (isRight) {
        RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 3.dp, bottomEnd = 3.dp)
    } else {
        RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 0.dp, bottomEnd = 0.dp)
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(PaperLight)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = if (isRight) 14.dp else 18.dp,
                    end = if (isRight) 18.dp else 14.dp,
                    top = 18.dp,
                    bottom = 18.dp
                )
        ) {
            when {
                isLoading -> {
                    repeat(8) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (index == 7) 0.6f else 1f)
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(PaperShade)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                else -> {
                    lines.forEach { line ->
                        StyledText(
                            text = line,
                            maxLines = 5,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Ink.copy(alpha = 0.86f),
                                fontFamily = FontFamily.Serif,
                                fontSize = 6.5.sp,
                                lineHeight = 10.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }

        // shading next to the crease
        Box(
            modifier = Modifier
                .align(if (isRight) Alignment.CenterStart else Alignment.CenterEnd)
                .width(14.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        if (isRight) listOf(Color(0x1F000000), Color(0x00000000))
                        else listOf(Color(0x00000000), Color(0x1F000000))
                    )
                )
        )

        // page stack along the outer edge
        Box(
            modifier = Modifier
                .align(if (isRight) Alignment.CenterEnd else Alignment.CenterStart)
                .width(3.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        if (isRight) listOf(Color(0x00000000), Color(0x1A000000))
                        else listOf(Color(0x1A000000), Color(0x00000000))
                    )
                )
        )
    }
}
