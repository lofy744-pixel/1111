package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.BannerItem
import com.example.models.AppStats
import com.example.ui.components.BannerSlider
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import com.example.ui.theme.CyberNeonYellow
import com.example.utils.WhatsAppHelper

@Composable
fun HomeScreen(
    banners: List<BannerItem>,
    appStats: AppStats,
    isArabic: Boolean,
    onNavigateToServices: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Welcome Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = if (isArabic) "مرحباً بك في 𝑵𝑬𝑶𝑽𝑨 𝑺𝑻𝑶𝑹𝑬 ⚡" else "Welcome to 𝑵𝑬𝑶𝑽𝑨 𝑺𝑻𝑶𝑹𝑬 ⚡",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberNeonPink
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = appStats.getWelcomeMessage(isArabic),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Banner Slider
        if (banners.isNotEmpty()) {
            BannerSlider(
                banners = banners,
                isArabic = isArabic,
                onBannerClick = { catId -> onCategorySelected(catId) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Quick Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Start Now
            CyberButton(
                text = if (isArabic) "ابدأ الآن" else "Start Now",
                onClick = onNavigateToServices,
                icon = Icons.Default.PlayArrow,
                gradientColors = listOf(CyberNeonPink, CyberNeonPurple),
                modifier = Modifier.weight(1f)
            )

            // Support
            CyberButton(
                text = if (isArabic) "الدعم" else "Support",
                onClick = { WhatsAppHelper.openSupportWhatsApp(context) },
                icon = Icons.Default.HeadsetMic,
                gradientColors = listOf(CyberNeonCyan, Color(0xFF00838F)),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Live Statistics Section Header
        Text(
            text = if (isArabic) "إحصائيات المتجر Live 📊" else "Live Store Statistics 📊",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Stats Grid Cards
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Clients Counter (Auto increments from 1211)
                StatCard(
                    title = if (isArabic) "عدد العملاء" else "Clients",
                    value = appStats.getDynamicClientsCount().toString(),
                    icon = Icons.Default.Group,
                    color = CyberNeonPink,
                    modifier = Modifier.weight(1f)
                )

                // Orders Count
                StatCard(
                    title = if (isArabic) "عدد الطلبات" else "Total Orders",
                    value = appStats.ordersCount.toString(),
                    icon = Icons.Default.ShoppingCart,
                    color = CyberNeonCyan,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Services Count
                StatCard(
                    title = if (isArabic) "عدد الخدمات" else "Services",
                    value = appStats.servicesCount.toString(),
                    icon = Icons.Default.ShoppingBag,
                    color = CyberNeonPurple,
                    modifier = Modifier.weight(1f)
                )

                // Visits Count
                StatCard(
                    title = if (isArabic) "عدد الزيارات" else "Store Visits",
                    value = appStats.visitsCount.toString(),
                    icon = Icons.Default.Visibility,
                    color = CyberNeonYellow,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        borderGradient = listOf(color.copy(alpha = 0.6f), color.copy(alpha = 0.2f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
