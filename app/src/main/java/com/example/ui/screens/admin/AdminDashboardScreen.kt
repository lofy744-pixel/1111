package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.ActivityLog
import com.example.models.AppStats
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.ServiceItem
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDashboardScreen(
    isArabic: Boolean,
    orders: List<OrderRequest>,
    services: List<ServiceItem>,
    categories: List<CategoryItem>,
    appStats: AppStats,
    logs: List<ActivityLog>,
    onNavigate: (String) -> Unit,
    onReloadData: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Calculations
    val totalOrders = orders.size.coerceAtLeast(appStats.ordersCount)
    val newOrders = orders.count { it.status == "PENDING" }
    val inProgressOrders = orders.count { it.status == "PROCESSING" }
    val completedOrders = orders.count { it.status == "COMPLETED" }
    val cancelledOrders = orders.count { it.status == "CANCELLED" }
    val reExecuteOrders = orders.count { it.status == "RE_EXECUTE" }

    val totalServices = services.size.coerceAtLeast(appStats.servicesCount)
    val totalCategories = categories.size
    val totalClients = appStats.getDynamicClientsCount()
    val totalVisits = appStats.visitsCount

    // Most requested service
    val topService = orders.groupBy { it.serviceName }
        .maxByOrNull { it.value.size }?.key ?: if (services.isNotEmpty()) services.first().getName(isArabic) else "شحن ببجي 60 شدة"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = null,
                        tint = CyberNeonPink,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isArabic) "لوحة التحكم الرئيسية" else "Admin Dashboard",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (isArabic) "متصل الآن بـ Firebase Firestore ⚡" else "Live Connected to Firestore ⚡",
                            fontSize = 12.sp,
                            color = CyberNeonCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onReloadData,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CyberNeonCyan.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync",
                            tint = CyberNeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CyberNeonPink.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = CyberNeonPink
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 1: Metrics Overview Cards
            Text(
                text = if (isArabic) "الإحصائيات المباشرة 📊" else "Real-time Metrics 📊",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (isArabic) "إجمالي الطلبات" else "Total Orders",
                    value = totalOrders.toString(),
                    icon = Icons.Default.ShoppingBag,
                    color = CyberNeonCyan,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "الطلبات الجديدة" else "New Orders",
                    value = newOrders.toString(),
                    icon = Icons.Default.HourglassTop,
                    color = Color(0xFFFFB300),
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "قيد التنفيذ" else "In Progress",
                    value = inProgressOrders.toString(),
                    icon = Icons.Default.Refresh,
                    color = CyberNeonPurple,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "الطلبات المكتملة" else "Completed",
                    value = completedOrders.toString(),
                    icon = Icons.Default.CheckCircle,
                    color = CyberNeonGreen,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "الطلبات الملغاة" else "Cancelled",
                    value = cancelledOrders.toString(),
                    icon = Icons.Default.Logout,
                    color = CyberNeonPink,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "إعادة التنفيذ" else "Re-execute",
                    value = reExecuteOrders.toString(),
                    icon = Icons.Default.Refresh,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "عدد الخدمات" else "Services Count",
                    value = totalServices.toString(),
                    icon = Icons.Default.MiscellaneousServices,
                    color = CyberNeonCyan,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "عدد الأقسام" else "Categories Count",
                    value = totalCategories.toString(),
                    icon = Icons.Default.Category,
                    color = CyberNeonPink,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "عدد العملاء" else "Total Clients",
                    value = totalClients.toString(),
                    icon = Icons.Default.People,
                    color = CyberNeonGreen,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (isArabic) "عدد الزيارات" else "Visits Count",
                    value = totalVisits.toString(),
                    icon = Icons.Default.Visibility,
                    color = CyberNeonPurple,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Top Service Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isArabic) "أكثر خدمة طلبًا ⭐" else "Most Requested Service ⭐",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = topService,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Quick Management Menu
            Text(
                text = if (isArabic) "إدارة النظام 🛠️" else "System Management 🛠️",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminNavTile(
                    title = if (isArabic) "إدارة الخدمات" else "Services Mgmt",
                    subtitle = "$totalServices خدمة",
                    icon = Icons.Default.MiscellaneousServices,
                    color = CyberNeonCyan,
                    onClick = { onNavigate("admin_services") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "إدارة الأقسام" else "Categories Mgmt",
                    subtitle = "$totalCategories أجزاء",
                    icon = Icons.Default.Category,
                    color = CyberNeonPink,
                    onClick = { onNavigate("admin_categories") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "إدارة الطلبات" else "Orders Mgmt",
                    subtitle = if (newOrders > 0) "$newOrders جديدة!" else "$totalOrders طلب",
                    icon = Icons.Default.ShoppingBag,
                    color = if (newOrders > 0) Color(0xFFFFB300) else CyberNeonGreen,
                    badgeCount = newOrders,
                    onClick = { onNavigate("admin_orders") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "وسائل الدفع" else "Payment Methods",
                    subtitle = "فودافون، انستاباي",
                    icon = Icons.Default.CreditCard,
                    color = CyberNeonPurple,
                    onClick = { onNavigate("admin_payments") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "إدارة التطبيق" else "App Management",
                    subtitle = "البنرات، الواتساب",
                    icon = Icons.Default.Smartphone,
                    color = CyberNeonCyan,
                    onClick = { onNavigate("admin_app_mgmt") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "الإعدادات" else "Settings",
                    subtitle = "الصيانة والنسخ",
                    icon = Icons.Default.Settings,
                    color = CyberNeonPink,
                    onClick = { onNavigate("admin_settings") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "بيانات الأدمن" else "Admin Profile",
                    subtitle = "تغيير الباسورد",
                    icon = Icons.Default.Person,
                    color = CyberNeonGreen,
                    onClick = { onNavigate("admin_profile") },
                    modifier = Modifier.weight(1f)
                )

                AdminNavTile(
                    title = if (isArabic) "سجل النشاط" else "Activity Logs",
                    subtitle = "${logs.size} عملية",
                    icon = Icons.Default.History,
                    color = CyberNeonPurple,
                    onClick = { onNavigate("admin_activity_logs") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 3: Recent Activity Preview
            Text(
                text = if (isArabic) "آخر النشاطات ⏱️" else "Recent Activity ⏱️",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (logs.isEmpty()) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isArabic) "لا توجد نشاطات مسجلة بعد" else "No recorded activities yet",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                logs.take(3).forEach { log ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = log.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${log.dateString} • ${log.timeString}",
                                    fontSize = 11.sp,
                                    color = CyberNeonCyan
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberNeonPink.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = log.actionType,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberNeonPink
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CyberDarkCard)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AdminNavTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CyberDarkCard)
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberNeonPink)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
