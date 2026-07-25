package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import com.example.viewmodels.NotificationType
import kotlinx.coroutines.delay

private data class NotificationStyle(
    val bgColor: Color,
    val borderColor: Color,
    val icon: ImageVector,
    val iconColor: Color
)

@Composable
fun AdminNotificationBanner(
    message: String?,
    type: NotificationType,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(message) {
        if (message != null) {
            delay(3500)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        if (message != null) {
            val style = when (type) {
                NotificationType.SUCCESS -> NotificationStyle(
                    CyberDarkCard,
                    CyberNeonGreen,
                    Icons.Default.CheckCircle,
                    CyberNeonGreen
                )
                NotificationType.ERROR -> NotificationStyle(
                    CyberDarkCard,
                    CyberNeonPink,
                    Icons.Default.Error,
                    CyberNeonPink
                )
                NotificationType.INFO -> NotificationStyle(
                    CyberDarkCard,
                    CyberNeonCyan,
                    Icons.Default.Info,
                    CyberNeonCyan
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(style.bgColor)
                    .border(1.5.dp, style.borderColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = style.icon,
                        contentDescription = null,
                        tint = style.iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun AdminConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "تأكيد",
    dismissText: String = "إلغاء",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (isDestructive) CyberNeonPink else CyberNeonCyan
            )
        },
        text = {
            Text(
                text = message,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    fontWeight = FontWeight.Bold,
                    color = if (isDestructive) CyberNeonPink else CyberNeonGreen
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        containerColor = CyberDarkCard,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.border(1.dp, CyberNeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    )
}
