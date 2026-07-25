package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.OrderRequest
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminOrdersScreen(
    isArabic: Boolean,
    orders: List<OrderRequest>,
    onUpdateOrderStatus: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf<String?>(null) } // null = All
    var orderToChangeStatus by remember { mutableStateOf<OrderRequest?>(null) }

    val filteredOrders = orders.filter { order ->
        val matchesStatus = selectedStatusFilter == null || order.status == selectedStatusFilter
        val matchesSearch = searchQuery.isBlank() ||
                order.orderId.contains(searchQuery, ignoreCase = true) ||
                order.phone.contains(searchQuery, ignoreCase = true) ||
                order.customerName.contains(searchQuery, ignoreCase = true) ||
                order.serviceName.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesSearch
    }

    val statusFilters = listOf(
        Pair("ALL", if (isArabic) "الكل" else "All"),
        Pair("PENDING", if (isArabic) "جديد" else "New"),
        Pair("PROCESSING", if (isArabic) "قيد التنفيذ" else "In Progress"),
        Pair("COMPLETED", if (isArabic) "مكتمل" else "Completed"),
        Pair("CANCELLED", if (isArabic) "ملغي" else "Cancelled"),
        Pair("RE_EXECUTE", if (isArabic) "إعادة تنفيذ" else "Re-execute")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = CyberNeonCyan
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isArabic) "إدارة الطلبات 📦" else "Orders Management 📦",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = if (isArabic) "ابحث برقم الطلب، الهاتف، أو الاسم..." else "Search ID, Phone or Name...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = CyberNeonCyan
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberNeonCyan,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(statusFilters, key = { it.first }) { (code, label) ->
                    val isSelected = (code == "ALL" && selectedStatusFilter == null) || selectedStatusFilter == code
                    val chipColor = when (code) {
                        "PENDING" -> Color(0xFFFFB300)
                        "PROCESSING" -> CyberNeonCyan
                        "COMPLETED" -> CyberNeonGreen
                        "CANCELLED" -> CyberNeonPink
                        "RE_EXECUTE" -> CyberNeonPurple
                        else -> CyberNeonPink
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) chipColor else CyberDarkCard)
                            .border(1.dp, if (isSelected) chipColor else MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .clickable { selectedStatusFilter = if (code == "ALL") null else code }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Orders list
            if (filteredOrders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "لا توجد طلبات هنا" else "No orders found",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredOrders, key = { it.orderId }) { order ->
                        val dateFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(order.timestamp))
                        val (statusText, statusBgColor) = getOrderStatusInfo(order.status, isArabic)

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { orderToChangeStatus = order }
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "#${order.orderId}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CyberNeonPink
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "($dateFormatted)",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(statusBgColor.copy(alpha = 0.2f))
                                            .border(1.dp, statusBgColor, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = statusText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = statusBgColor
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "الخدمة: ${order.serviceName} (الكمية: ${order.quantity})",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "الإجمالي: ${order.totalPrice} جنيه مصري",
                                    fontSize = 13.sp,
                                    color = CyberNeonCyan,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${order.customerName} (${order.phone})",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (order.accountIdOrLink.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "الـ ID/الرابط: ${order.accountIdOrLink}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CyberNeonPurple
                                    )
                                }

                                if (order.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ملاحظات: ${order.notes}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (isArabic) "انقر لتغيير حالة الطلب 👈" else "Tap to change order status 👈",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberNeonCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        // Status Change Dialog
        if (orderToChangeStatus != null) {
            val order = orderToChangeStatus!!
            AlertDialog(
                onDismissRequest = { orderToChangeStatus = null },
                title = {
                    Text(
                        text = if (isArabic) "تغيير حالة الطلب #${order.orderId}" else "Change Order Status #${order.orderId}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CyberNeonPink
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatusOptionItem(
                            title = if (isArabic) "جديد (PENDING)" else "New (PENDING)",
                            color = Color(0xFFFFB300),
                            onClick = {
                                onUpdateOrderStatus(order.orderId, "PENDING")
                                orderToChangeStatus = null
                            }
                        )

                        StatusOptionItem(
                            title = if (isArabic) "قيد التنفيذ (PROCESSING)" else "In Progress (PROCESSING)",
                            color = CyberNeonCyan,
                            onClick = {
                                onUpdateOrderStatus(order.orderId, "PROCESSING")
                                orderToChangeStatus = null
                            }
                        )

                        StatusOptionItem(
                            title = if (isArabic) "مكتمل (COMPLETED)" else "Completed (COMPLETED)",
                            color = CyberNeonGreen,
                            onClick = {
                                onUpdateOrderStatus(order.orderId, "COMPLETED")
                                orderToChangeStatus = null
                            }
                        )

                        StatusOptionItem(
                            title = if (isArabic) "ملغي (CANCELLED)" else "Cancelled (CANCELLED)",
                            color = CyberNeonPink,
                            onClick = {
                                onUpdateOrderStatus(order.orderId, "CANCELLED")
                                orderToChangeStatus = null
                            }
                        )

                        StatusOptionItem(
                            title = if (isArabic) "إعادة تنفيذ (RE_EXECUTE)" else "Re-execute (RE_EXECUTE)",
                            color = CyberNeonPurple,
                            onClick = {
                                onUpdateOrderStatus(order.orderId, "RE_EXECUTE")
                                orderToChangeStatus = null
                            }
                        )
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { orderToChangeStatus = null }) {
                        Text(if (isArabic) "إلغاء" else "Cancel")
                    }
                },
                containerColor = CyberDarkCard,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.border(1.dp, CyberNeonCyan, RoundedCornerShape(20.dp))
            )
        }
    }
}

@Composable
private fun StatusOptionItem(
    title: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

private fun getOrderStatusInfo(status: String, isArabic: Boolean): Pair<String, Color> {
    return when (status) {
        "PENDING" -> Pair(if (isArabic) "جديد" else "New", Color(0xFFFFB300))
        "PROCESSING" -> Pair(if (isArabic) "قيد التنفيذ" else "Processing", CyberNeonCyan)
        "COMPLETED" -> Pair(if (isArabic) "مكتمل" else "Completed", CyberNeonGreen)
        "CANCELLED" -> Pair(if (isArabic) "ملغي" else "Cancelled", CyberNeonPink)
        "RE_EXECUTE" -> Pair(if (isArabic) "إعادة تنفيذ" else "Re-execute", CyberNeonPurple)
        else -> Pair(status, CyberNeonCyan)
    }
}
