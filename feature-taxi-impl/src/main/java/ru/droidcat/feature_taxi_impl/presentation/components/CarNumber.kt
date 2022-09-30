package ru.droidcat.feature_taxi_impl.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CarNumber(
    number: String,
    region: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .border(
                border = BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.onBackground
                ),
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(all = 4.dp),
            text = number,
            style = MaterialTheme.typography.bodyLarge
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
        Text(
            modifier = Modifier.padding(all = 4.dp),
            text = region,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}