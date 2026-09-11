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
private val PaperShade = Color(0xFFF3EFE7)
private val Ink = Color(0xFF2A241D)
private val InkSoft = Color(0xFF6B6055)

/**
 * A 3D open-book preview: two white pages side by side, a central spine crease,
 * curved page edges and a warm floating shadow. Shows real text from the book.
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
            .aspectRatio(1.35f)
            .padding(horizontal = 4.dp)
    ) {
        // outer page stack, slightly wider and offset for depth
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.985f)
                .fillMaxHeight(0.965f)
                .shadow(
                    elevation = 26.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = Color(0x33453A2C),
                    spotColor = Color(0x55453A2C)
                )
                .clip(RoundedCornerShape(14.dp))
                .background(PaperShade)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp))
                .background(PaperLight)
                .clickable(onClick = onClick)
        ) {
            BookPage(
                lines = leftPage,
                isLoading = isLoading,
                alignEnd = false,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            BookPage(
                lines = rightPage,
                isLoading = isLoading,
                alignEnd = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        // central spine crease
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(30.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x00000000),
                            Color(0x1A2A241D),
                            Color(0x452A241D),
                            Color(0x1A2A241D),
                            Color(0x00000000)
                        )
                    )
                )
        )

        // soft top gloss
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x14FFFFFF), Color(0x00FFFFFF), Color(0x0F2A241D))
                    )
                )
        )
    }
}

@Composable
private fun BookPage(
    lines: List<String>,
    isLoading: Boolean,
    alignEnd: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = if (alignEnd) 18.dp else 22.dp,
                    end = if (alignEnd) 22.dp else 18.dp,
                    top = 22.dp,
                    bottom = 22.dp
                )
        ) {
            when {
                isLoading -> {
                    repeat(6) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (index == 5) 0.6f else 1f)
                                .height(9.dp)
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
                            maxLines = 4,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Ink.copy(alpha = 0.86f),
                                fontFamily = FontFamily.Serif,
                                fontSize = 9.5.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }

        // page edge shading next to the crease
        Box(
            modifier = Modifier
                .align(if (alignEnd) Alignment.CenterStart else Alignment.CenterEnd)
                .width(10.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        if (alignEnd) listOf(Color(0x142A241D), Color(0x00000000))
                        else listOf(Color(0x00000000), Color(0x142A241D))
                    )
                )
        )

        if (!isLoading && lines.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                StyledText(
                    text = "",
                    style = MaterialTheme.typography.bodySmall.copy(color = InkSoft)
                )
            }
        }
    }
}
