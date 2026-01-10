package com.roblescode.backgrounderaser.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.roblescode.backgrounderaser.ui.components.ActionButtons
import com.roblescode.backgrounderaser.ui.components.ComparisonView
import com.roblescode.backgrounderaser.ui.components.EmptyState
import com.roblescode.backgrounderaser.ui.components.ErrorCard
import com.roblescode.backgrounderaser.ui.components.ImageCard
import com.roblescode.backgrounderaser.ui.components.LoadingState
import com.roblescode.backgrounderaser.ui.utils.decodeSampledBitmap
import com.roblescode.bgeraser.BackgroundEraser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundEraserScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var resultBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var showComparison by rememberSaveable { mutableStateOf(false) }

    val eraser = remember { BackgroundEraser(context) }

    DisposableEffect(Unit) {
        onDispose { eraser.close() }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            resultBitmap = null
            error = null
            showComparison = false

            originalBitmap = uri?.let {
                decodeSampledBitmap(
                    context = context,
                    uri = it,
                    maxSize = 2048
                )
            }
        }
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Background Eraser",
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    targetState = when {
                        isLoading -> "loading"
                        resultBitmap != null && showComparison -> "comparison"
                        resultBitmap != null -> "result"
                        originalBitmap != null -> "original"
                        else -> "empty"
                    },
                    label = "image_transition"
                ) { state ->
                    when (state) {
                        "loading" -> LoadingState()
                        "comparison" -> ComparisonView(originalBitmap, resultBitmap)
                        "result" -> ImageCard(resultBitmap, "Result")
                        "original" -> ImageCard(originalBitmap, "Original Image")
                        else -> EmptyState()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = error != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    ErrorCard(
                        message = error ?: "",
                        onDismiss = { error = null }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ActionButtons(
                    hasOriginal = originalBitmap != null,
                    hasResult = resultBitmap != null,
                    isLoading = isLoading,
                    showComparison = showComparison,
                    onSelectImage = {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onRemoveBackground = {
                        scope.launch {
                            isLoading = true
                            error = null

                            eraser.clearBackground(originalBitmap!!)
                                .onSuccess { result ->
                                    resultBitmap = result
                                }
                                .onFailure {
                                    error = it.message ?: "Unknown error"
                                }

                            isLoading = false
                        }
                    },
                    onToggleComparison = { showComparison = !showComparison },
                    onReset = {
                        originalBitmap = null
                        resultBitmap = null
                        error = null
                        showComparison = false
                    }
                )
            }
        }
    }
}