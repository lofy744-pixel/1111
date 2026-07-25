package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.PaymentMethod
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple

@Composable
fun PaymentScreen(
    paymentMethods: List<PaymentMethod>,
    isArabic: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (isArabic) "وسائل الدفع المتاحة 💳" else "Available Payment Methods 💳",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isArabic)
                "حويل المبلغ المطلوب إلى أحد الأرقام الموضحة أدناه مع حفظ لقطة الشاشة"
            else
                "Transfer the required amount to one of the numbers below and keep screenshot",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (paymentMethods.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (isArabic) "جاري تحميل وسائل الدفع..." else "Loading payment methods...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(paymentMethods) { method ->
                    PaymentMethodCard(
                        method = method,
                        isArabic = isArabic,
                        onCopyNumber = { copyToClipboard(context, method.number, isArabic) },
                        onShareNumber = { shareNumber(context, method.getName(isArabic), method.number) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodCard(
    method: PaymentMethod,
    isArabic: Boolean,
    onCopyNumber: () -> Unit,
    onShareNumber: () -> Unit
) {
    val brandColor = when (method.iconType) {
        "vodafone" -> Color(0xFFE50000)
        "etisalat" -> Color(0xFF72BF44)
        "instapay" -> CyberNeonCyan
        else -> CyberNeonPink
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderGradient = listOf(brandColor.copy(alpha = 0.7f), CyberNeonPurple.copy(alpha = 0.3f))
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(brandColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getPaymentIcon(method.iconType),
                        contentDescription = method.getName(isArabic),
                        tint = brandColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = method.getName(isArabic),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = method.number,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = brandColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row: Copy Number & Share Number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Copy Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brandColor.copy(alpha = 0.15f))
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconButton(onClick = onCopyNumber, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = brandColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "نسخ الرقم" else "Copy Number",
                            color = brandColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Share Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberNeonPurple.copy(alpha = 0.15f))
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconButton(onClick = onShareNumber, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = CyberNeonPurple,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "مشاركة الرقم" else "Share Number",
                            color = CyberNeonPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Instructions
            Text(
                text = method.getInstructions(isArabic),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )
        }
    }
}

private fun getPaymentIcon(type: String) = when (type) {
    "vodafone" -> Icons.Default.PhoneAndroid
    "etisalat" -> Icons.Default.AccountBalanceWallet
    "instapay" -> Icons.Default.CreditCard
    else -> Icons.Default.AccountBalanceWallet
}

private fun copyToClipboard(context: Context, text: String, isArabic: Boolean) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Payment Number", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(
        context,
        if (isArabic) "تم نسخ الرقم $text بنجاح" else "Copied $text to clipboard",
        Toast.LENGTH_SHORT
    ).show()
}

private fun shareNumber(context: Context, title: String, number: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, "$title - NEOVA STORE:\n$number")
    }
    context.startActivity(Intent.createChooser(intent, "مشاركة الوسيلة"))
}
