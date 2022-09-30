package ru.droidcat.feature_taxi_impl.presentation.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.droidcat.feature_taxi_impl.R

@Composable
fun CarImage(
    image: Bitmap?,
    imageLoading: Boolean,
    loadImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        AnimatedVisibility(
            visible = imageLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(
            visible = image != null && !imageLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            image?.asImageBitmap()?.let {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    bitmap = it,
                    contentDescription = stringResource(R.string.car_image_description)
                )
            }
        }
        AnimatedVisibility(
            visible = image == null && !imageLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(stringResource(R.string.error_image_not_loaded))
                    Button(
                        onClick = { loadImage() }
                    ) {
                        Text(stringResource(R.string.try_again_button_text))
                    }
                }
            }
        }
    }
}