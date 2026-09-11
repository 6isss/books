/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.reading_now.AppleInk
import ua.acclorite.book_story.ui.reading_now.AppleInkSoft
import ua.acclorite.book_story.ui.reading_now.AppleScreenHeader
import ua.acclorite.book_story.ui.reading_now.Rule

@Composable
fun LibraryAppleHeader(
    onEdit: () -> Unit,
    onCollections: () -> Unit,
    navigateToSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppleScreenHeader(
            title = stringResource(id = R.string.library_screen),
            navigateToSettings = navigateToSettings,
            trailing = {
                StyledText(
                    text = stringResource(id = R.string.edit),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = AppleInk,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onEdit() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCollections() }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Rule)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CollectionsBookmark,
                    contentDescription = null,
                    tint = AppleInkSoft,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(17.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            StyledText(
                text = stringResource(id = R.string.collections),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = AppleInk,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppleInkSoft,
                modifier = Modifier.size(20.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Rule
        )
    }
}
