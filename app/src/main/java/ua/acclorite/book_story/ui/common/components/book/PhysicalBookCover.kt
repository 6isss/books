/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.book

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

private val DefaultTint = Color(0xFF6B5F4E)

/**
 * A cover shown as a physical book: rounded page-edge corners on the outer side,
 * a tight spine on the inner side and a soft drop shadow tinted with the
 * dominant colour of the artwork.
 */
@Composable
fun PhysicalBookCover(
    uri: Uri?,
    modifier: Modifier = Modifier,
    elevation: Dp = 14.dp,
    cornerRadius: Dp = 6.dp
) {
    var tint by remember(uri) { mutableStateOf(DefaultTint) }
    val shadowColor by animateColorAsState(targetValue = tint, label = "cover_shadow")

    val shape = RoundedCornerShape(
        topStart = 2.dp,
        bottomStart = 2.dp,
        topEnd = cornerRadius,
        bottomEnd = cornerRadius
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = shadowColor.copy(alpha = 0.55f),
                spotColor = shadowColor.copy(alpha = 0.85f)
            )
            .clip(shape)
            .background(Color(0xFFE9E3D8))
    ) {
        if (uri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .allowHardware(false)
                    .crossfade(120)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onSuccess = { state ->
                    val bitmap = (state.result.drawable as? BitmapDrawable)?.bitmap
                    if (bitmap != null) {
                        tint = bitmap.averageColor()
                    }
                }
            )
        } else {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(0.35f),
                tint = DefaultTint.copy(alpha = 0.4f)
            )
        }

        // spine shading along the inner (left) edge
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(9.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x59000000),
                            Color(0x1F000000),
                            Color(0x00000000)
                        )
                    )
                )
        )

        // page edges along the outer (right) edge
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(4.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x1A000000),
                            Color(0xCCFFFFFF),
                            Color(0x66FFFFFF)
                        )
                    )
                )
        )

        // soft top-left light
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x1AFFFFFF), Color(0x00FFFFFF), Color(0x14000000))
                    )
                )
        )
    }
}

private fun android.graphics.Bitmap.averageColor(): Color {
    return try {
        val scaled = android.graphics.Bitmap.createScaledBitmap(this, 8, 8, true)
        var r = 0L
        var g = 0L
        var b = 0L
        var count = 0
        for (x in 0 until scaled.width) {
            for (y in 0 until scaled.height) {
                val pixel = scaled.getPixel(x, y)
                r += (pixel shr 16) and 0xFF
                g += (pixel shr 8) and 0xFF
                b += pixel and 0xFF
                count++
            }
        }
        if (count == 0) return DefaultTint
        Color(
            red = (r / count).toInt().coerceIn(0, 255),
            green = (g / count).toInt().coerceIn(0, 255),
            blue = (b / count).toInt().coerceIn(0, 255)
        )
    } catch (e: Exception) {
        DefaultTint
    }
}
