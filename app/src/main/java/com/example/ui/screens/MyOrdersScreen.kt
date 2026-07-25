package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
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
import com.example.models.OrderRequest
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import com.example.utils.WhatsAppHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyOrdersScreen(
    orders: List<OrderRequest>,
    isArabic: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (isArabic) "قائمة طلباتي 📦" else "My Orders History 📦",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isArabic)
                "سجل جميع طلباتك الشحن والخدمات الرقمية وحالتها المباشرة"
            else
                "Record of all your game top-ups & digital services orders",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = null,
                        tint = CyberNeonPink,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isArabic) "لا توجد طلبات سابقة حتى الآن" else "No previous orders yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(orders, key = { it.orderId }) { order ->
                    OrderCard(
                        order = order,
                        isArabic = isArabic,
                        onSendWhatsAppAgain = {
                            WhatsAppHelper.sendOrderWhatsApp(context, order)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderRequest,
    isArabic: Boolean,
    onSendWhatsAppAgain: () -> Unit
) {
    val statusColor = when (order.status) {
        "COMPLETED" -> Color(0xFF00FF66)
        "PROCESSING" -> CyberNeonCyan
        else -> CyberNeonPink
    }

    val statusText = when (order.status) {
        "COMPLETED" -> if (isArabic) "مكتمل" else "Completed"
        "PROCESSING" -> if (isArabic) "قيد التنفيذ" else "In Progress"
        else -> if (isArabic) "قيد الانتظار" else "Pending"
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(order.timestamp))

    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${order.orderId}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyberNeonPink
                )

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.serviceName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isArabic) "الكمية: ${order.quantity} | الإجمالي: ${order.totalPrice} ج.م"
                else "Qty: ${order.quantity} | Total: ${order.totalPrice} EGP",
                fontSize = 13.sp,
                color = CyberNeonCyan,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formattedDate,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Re-open WhatsApp button
            CyberButton(
                text = if (isArabic) "فتح في واتساب 💬" else "Open in WhatsApp 💬",
                onClick = onSendWhatsAppAgain,
                icon = Icons.Default.Send,
                gradientColors = listOf(CyberNeonCyan, Color(0xFF00838F)),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            )
        }
    }
}
