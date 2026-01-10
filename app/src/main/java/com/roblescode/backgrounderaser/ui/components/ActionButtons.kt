package com.roblescode.backgrounderaser.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtons(
    hasOriginal: Boolean,
    hasResult: Boolean,
    isLoading: Boolean,
    showComparison: Boolean,
    onSelectImage: () -> Unit,
    onRemoveBackground: () -> Unit,
    onToggleComparison: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedContent(
            targetState = when {
                !hasOriginal -> "select"
                hasResult -> "done"
                else -> "process"
            },
            label = "main_button"
        ) { state ->
            when (state) {
                "select" -> PrimaryButton(
                    text = "Select Image",
                    icon = Icons.Default.Search,
                    onClick = onSelectImage
                )

                "process" -> PrimaryButton(
                    text = "Remove Background",
                    icon = Icons.Default.Create,
                    onClick = onRemoveBackground,
                    enabled = !isLoading
                )

                "done" -> PrimaryButton(
                    text = "Background Removed!",
                    icon = Icons.Default.CheckCircle,
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }

        AnimatedVisibility(visible = hasResult) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onToggleComparison,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (showComparison) "View result" else "Compare")
                }

                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("New image")
                }
            }
        }
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = colors
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}