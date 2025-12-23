package com.navigo.navilite.feature.details.presentation.composible

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navigo.navilite.R

@Composable
internal fun StartNavigationButton(
    modifier: Modifier = Modifier,
    onStartNavigation: () -> Unit,
    isNavigating: Boolean
) {
    Button(
        onClick = onStartNavigation,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isNavigating) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = if (isNavigating) {
                stringResource(R.string.restart_navigation)
            } else {
                stringResource(R.string.start_navigation)
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        )
    }
}